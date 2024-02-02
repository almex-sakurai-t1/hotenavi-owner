/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ���[�U��{���擾�N���X
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserElect;

/**
 * ���I���[�U�擾�N���X�B ���[�U����󋵂̃f�[�^���擾����@�\��񋟂���
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/26
 */
public class UserElect implements Serializable
{
    private static final long serialVersionUID = -7658827367902506182L;

    private int               userElectCount;
    private DataUserElect[]   m_userElect;

    /**
     * �f�[�^�����������܂��B
     */
    public UserElect()
    {
        userElectCount = 0;
    }

    /** ���[�U�����񌏐��擾 **/
    public int getCount()
    {
        return(userElectCount);
    }

    /** ���[�U������擾 **/
    public DataUserElect[] getUserElect()
    {
        return(m_userElect);
    }

    /**
     * ��������擾����iID����j
     * 
     * @param userId �J�e�S��
     * @param seq �Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserElect(String userId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        if ( seq < 0 )
        {
            return(false);
        }
        query = "SELECT SQL_NO_CACHE * FROM hh_user_elect";
        query = query + " WHERE user_id = ?";
        query = query + " AND seq = ?";
        query = query + " ORDER BY seq DESC, application_count";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, seq );

            ret = getUserElectSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserElect] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ��������擾����iID,�L����������j
     * 
     * @param userId �J�e�S��
     * @param seq �Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserElect(String userId, int startDate, int endDate)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        if ( startDate < 0 || endDate < 0 )
        {
            return(false);
        }
        query = "SELECT SQL_NO_CACHE * FROM hh_user_elect";
        query = query + " WHERE user_id = ?";
        query = query + " AND input_date >= ?";
        query = query + " AND input_date <= ?";
        query = query + " AND status_flag >= 3";
        query = query + " AND user_name <> '' ";
        query = query + " AND zip_code <> '' ";
        query = query + " AND pref_name <> '' ";
        query = query + " AND address1 <> '' ";
        query = query + " AND tel1 <> '' ";
        query = query + " ORDER BY input_date DESC, input_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, startDate );
            prestate.setInt( 3, endDate );

            ret = getUserElectSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserElect] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ��������擾����
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getUserElectSub(PreparedStatement prestate)
    {
        ResultSet result = null;
        int count;
        int i;

        i = 0;
        count = 0;
        userElectCount = 0;
        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    userElectCount = result.getRow();
                }
                this.m_userElect = new DataUserElect[this.userElectCount];
                for( i = 0 ; i < userElectCount ; i++ )
                {
                    m_userElect[i] = new DataUserElect();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ���I�f�[�^���̐ݒ�
                    this.m_userElect[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserElectSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userElectCount > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * �������o�^����
     * 
     * @param userId �J�e�S��
     * @param seq �Ǘ��ԍ�
     * @param applicationCount �������
     * @param loopCount �J��Ԃ���
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setUserElect(String userId, int seq, int applicationCount, int loopCount)
    {
        boolean ret;
        DataUserElect due;

        if ( userId == null )
            return(false);
        if ( seq < 0 )
            return(false);
        if ( applicationCount < 0 )
            return(false);
        if ( loopCount < 0 )
            return(false);

        due = new DataUserElect();
        ret = due.getData( userId, seq, applicationCount );

        // �f�[�^�擾��AloopCount�̕������f�[�^�o�^���s��
        if ( ret != false )
        {
            due.insertData( loopCount );
        }

        return(ret);
    }

    /**
     * ����������擾����iID,seq����j
     * 
     * @param userId ���[�U�[ID
     * @param seq �Ǘ��ԍ�
     * @return ��������(-1:�擾�G���[)
     */
    public int getMaxApplicationCount(String userId, int seq)
    {
        int maxSeq;
        String query;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement prestate = null;

        maxSeq = 0;
        if ( userId == null )
        {
            return(-1);
        }
        if ( seq < 0 )
        {
            return(-1);
        }
        query = "SELECT SQL_NO_CACHE MAX(application_count) FROM hh_user_elect";
        query = query + " WHERE user_id = ?";
        query = query + " AND seq = ?";

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
                    maxSeq = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getMaxApplicationCount] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(maxSeq);
    }

    /**
     * ���I�҃f�[�^���擾����
     * 
     * @param seq �Ǘ��ԍ�
     * @return ��������(-1:�擾�G���[)
     */
    public boolean getUserElectWinner(int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( seq < 0 )
            return(false);

        query = "SELECT SQL_NO_CACHE * FROM hh_user_elect";
        query = query + " WHERE seq = ?";
        query = query + " AND status_flag = 5";
        query = query + " GROUP BY user_id";

        ret = false;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, seq );

            ret = getUserElectSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserElectWinner] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        if ( ret == false )
        {
            this.userElectCount = 0;
        }
        return(ret);
    }

    /**
     * �撅���̉�������擾����iID,�L����������j
     * 
     * @param userId �J�e�S��
     * @param startDate �J�n��
     * @param endDate �I����
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserElectByFirstCome(String userId, int startDate, int endDate)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        if ( startDate < 0 || endDate < 0 )
        {
            return(false);
        }
        query = "SELECT SQL_NO_CACHE hh_user_elect.* FROM hh_user_elect, hh_master_present";
        query = query + " WHERE hh_user_elect.user_id = ?";
        query = query + " AND hh_user_elect.input_date >= ?";
        query = query + " AND hh_user_elect.input_date <= ?";
        query = query + " AND hh_user_elect.seq = hh_master_present.seq";
        query = query + " AND hh_master_present.kind_flag = 1";
        query = query + " ORDER BY input_date DESC, input_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, startDate );
            prestate.setInt( 3, endDate );

            ret = getUserElectSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserElect] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �撅���̉�������擾����iID,�L����������j
     * 
     * @param userId �J�e�S��
     * @param startDate �J�n��
     * @param endDate �I����
     * @param offerHotel
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserElectByOfferHotel(String userId, int offerHotel, int prefId, int startDate, int endDate)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        if ( startDate < 0 || endDate < 0 || offerHotel < 0 )
        {
            return(false);
        }
        query = "SELECT SQL_NO_CACHE hh_user_elect.* FROM hh_user_elect, hh_master_present";
        query = query + " WHERE hh_user_elect.user_id = ?";
        query = query + " AND hh_user_elect.input_date >= ?";
        query = query + " AND hh_user_elect.input_date <= ?";
        query = query + " AND hh_user_elect.seq = hh_master_present.seq";
        query = query + " AND hh_master_present.offer_hotel = ?";
        query = query + " AND hh_master_present.pref_id = ?";
        query = query + " AND hh_master_present.kind_flag = 1";
        query = query + " ORDER BY input_date DESC, input_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, startDate );
            prestate.setInt( 3, endDate );
            prestate.setInt( 4, offerHotel );
            prestate.setInt( 5, prefId );

            ret = getUserElectSub( prestate );
            Logging.info( "[getUserElect] bool=" + ret );
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserElect] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ����҃f�[�^���擾����
     * 
     * @param seq �Ǘ��ԍ�
     * @return ��������(-1:�擾�G���[)
     */
    public int getUserElectBySeq(int seq)
    {
        int electCount;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;

        if ( seq < 0 )
            return(-1);
        electCount = 0;

        query = "SELECT COUNT(*) FROM hh_user_elect";
        query = query + " WHERE seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, seq );

            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    electCount = result.getInt( 1 );
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserElectWinner] Exception=" + e.toString() );
            electCount = -1;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(electCount);
    }

    /**
     * ��������擾����iID,�L����������j
     * 
     * @param userId �J�e�S��
     * @param seq �Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserElectStay(String userId, int startDate, int endDate)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId == null )
        {
            return(false);
        }
        if ( startDate < 0 || endDate < 0 )
        {
            return(false);
        }
        query = "SELECT SQL_NO_CACHE * FROM hh_user_elect";
        query = query + " WHERE user_id = ?";
        query = query + " AND input_date >= ?";
        query = query + " AND input_date <= ?";
        query = query + " AND status_flag >= 3";
        query = query + " ORDER BY input_date DESC, input_time DESC";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, startDate );
            prestate.setInt( 3, endDate );

            ret = getUserElectSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getUserElect] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

}
