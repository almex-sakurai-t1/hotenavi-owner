/*
 * @(#)OwnerLoginInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 �I�[�i�[���O�C�����擾�N���X
 */

package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotenaviHotel;
import jp.happyhotel.data.DataOwnerUser;
import jp.happyhotel.data.DataOwnerUserLock;
import jp.happyhotel.data.DataOwnerUserSecurity;

/**
 * �I�[�i�[���O�C�����擾�N���X�B �I�[�i�[���O�C�������擾����@�\��񋟂���
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.00 2007/12/03
 */
public class OwnerLoginInfo implements Serializable
{
    /**
	 *
	 */
    private static final long     serialVersionUID = 705628811172679729L;

    private int                   ownerUserCount;
    private DataOwnerUser         ownerUser;
    private DataOwnerUserSecurity ownerUserSecurity;
    private DataHotenaviHotel     hotenaviHotel;

    /**
     * �f�[�^�����������܂��B
     */
    public OwnerLoginInfo()
    {
        ownerUserCount = 0;
        ownerUser = new DataOwnerUser();
        ownerUserSecurity = new DataOwnerUserSecurity();
        hotenaviHotel = new DataHotenaviHotel();
    }

    /** �I�[�i�[��{��񌏐��擾 **/
    public int getCount()
    {
        return(ownerUserCount);
    }

    /** �I�[�i�[��{���擾 **/
    public DataOwnerUser getUserInfo()
    {
        return(ownerUser);
    }

    /** �I�[�i�[�A�N�Z�X�����擾 **/
    public DataOwnerUserSecurity getUserSecurityInfo()
    {
        return(ownerUserSecurity);
    }

    /** �z�e�i�r�z�e�����擾 **/
    public DataHotenaviHotel getHotenaviHotel()
    {
        return(hotenaviHotel);
    }

    /**
     * �I�[�i�[��{�����擾����iID����j
     * 
     * @param netId �l�b�g���[�NID
     * @param netPass �l�b�g���[�N�p�X���[�h
     * @param userId ���[�UID
     * @param userPass ���[�U�p�X���[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getLogin(String netId, String netPass, String userId, String userPass)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT owner_user.* FROM owner_user,hotel";
        query = query + " WHERE hotel.owner_user = ? AND hotel.owner_password = ?";
        query = query + " AND owner_user.loginid = ? AND owner_user.passwd_pc = ?";
        query = query + " AND hotel.hotel_id = owner_user.hotelid";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, netId );
            prestate.setString( 2, netPass );
            prestate.setString( 3, userId );
            prestate.setString( 4, userPass );

            ret = getLoginSub( prestate );
            if ( ret != false )
            {
                // �z�e�i�r�z�e�����擾
                hotenaviHotel = new DataHotenaviHotel();
                hotenaviHotel.getData( this.ownerUser.getHotelId() );
                // �Z�L�����e�B���擾
                ownerUserSecurity = new DataOwnerUserSecurity();
                ownerUserSecurity.getData( this.ownerUser.getHotelId(), this.ownerUser.getUserId() );
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[OwnerLoginInfo.getLogin] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �I�[�i�[��{�����擾����i�N�b�L�[����j
     * 
     * @param cookieValue �N�b�L�[�l
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getLoginByCookie(String cookieValue)
    {
        boolean ret;
        String query;
        String hotelId;
        String userId;
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( cookieValue == null )
        {
            return(false);
        }

        hotelId = cookieValue.substring( 0, cookieValue.indexOf( ":" ) );
        userId = cookieValue.substring( cookieValue.indexOf( ":" ) + 1 );

        query = "SELECT owner_user.* FROM owner_user";
        query = query + " WHERE owner_user.hotelid = ? AND owner_user.userid = ?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, hotelId );
            prestate.setString( 2, userId );

            ret = getLoginSub( prestate );
            if ( ret != false )
            {
                // �z�e�i�r�z�e�����擾
                hotenaviHotel = new DataHotenaviHotel();
                hotenaviHotel.getData( this.ownerUser.getHotelId() );
                // �Z�L�����e�B���擾
                ownerUserSecurity = new DataOwnerUserSecurity();
                ownerUserSecurity.getData( this.ownerUser.getHotelId(), this.ownerUser.getUserId() );
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[OwnerLoginInfo.getLoginByCookie] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �I�[�i�[��{���̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getLoginSub(PreparedStatement prestate)
    {
        ResultSet result = null;

        try
        {
            result = prestate.executeQuery();

            if ( result != null )
            {
                ownerUser = new DataOwnerUser();

                if ( result.next() != false )
                {
                    ownerUserCount = 1;
                    // �I�[�i�[��{�f�[�^���̐ݒ�
                    this.ownerUser.setData( result );
                }
                else
                {
                    ownerUserCount = 0;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[OwnerLoginInfo.getLoginSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( ownerUserCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * �A�����O�C�����s�`�F�b�N
     * 
     * @param userId �V���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean userLoginHistoryCheck(String hotelId, String loginId)
    {
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int userId = 0;
        try
        {
            query = "SELECT owner_user.userid FROM owner_user";
            query = query + " WHERE owner_user.hotelid = ? AND owner_user.loginid = ?";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            int i = 1;
            prestate.setString( i++, hotelId );
            prestate.setString( i++, loginId );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                userId = result.getInt( "userid" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerLoginInfo.userLoginHistoryCheck] Exception=" + e.toString(), e );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( userId == 0 )
        {
            return true;
            // loginId �����݂��Ȃ��B���b�N�Ƃ͊֌W�Ȃ��̂ŁA�Ƃ肠����true�ł͕Ԃ�
        }
        else
        {
            int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            DataOwnerUserLock lock = new DataOwnerUserLock();
            if ( lock.getData( hotelId, userId ) )
            {
                // �A�J�E���g�����b�N����Ă���
                if ( lock.getLockStatus() == 1 && lock.getLockDate() >= nowDate && lock.getLockTime() >= nowTime )
                {
                    return false;
                }
                // ������
                lock.setLockStatus( 0 );
                lock.setLockDate( 0 );
                lock.setLockTime( 0 );
                lock.updateData( hotelId, userId );
            }

            int[] time = DateEdit.addSec( nowDate, nowTime, -900 );
            int nDate = time[0];
            int nTime = time[1];

            // �����ɂ���Ă����̂�final��t���܂���
            query = "SELECT * FROM owner_user_log"
                    + " WHERE hotelid = ?"
                    + " AND userid = ?"
                    + " AND log_level != 1";

            // 15���O�͖{���̏ꍇ
            if ( time[0] == nowDate )
            {
                query += " AND login_date = ?"
                        + " AND login_time BETWEEN ? AND ?";
            }
            // 15���O�͍���̏ꍇ
            else
            {
                query += " AND login_date >= ?"
                        + " AND (login_time  >= ? OR login_time <= ?)";
            }
            query += " ORDER BY login_date DESC,login_time DESC"
                    + " LIMIT 1";

            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                int i = 1;
                prestate.setString( i++, hotelId );
                prestate.setInt( i++, userId );
                prestate.setInt( i++, nDate );
                prestate.setInt( i++, nTime );
                prestate.setInt( i++, nowTime );
                result = prestate.executeQuery();

                if ( result.next() )
                {
                    int loginDate = Integer.parseInt( result.getString( "login_date" ).replace( "-", "" ) );
                    int loginTime = Integer.parseInt( result.getString( "login_time" ).replace( ":", "" ) );
                    return isFailOverFiveTimes( hotelId, userId, loginDate, loginTime, lock, connection );
                }
                return true;

            }
            catch ( Exception e )
            {
                Logging.error( "[OwnerLoginInfo.userLoginHistoryCheck] Exception=" + e.toString(), e );
                return false;
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }
    }

    /**
     * �A�����O�C��5��`�F�b�N
     * 
     * @param userId ���[�UID
     * @param loginDate ���t
     * @param loginTime ����
     * @param lock DataUserLock
     * @param connection
     * @return �������� TRUE:����,FALSE:�ُ�(5��ȏ㎸�s)
     */
    public boolean isFailOverFiveTimes(
            String hotelId, int userId, int loginDate, int loginTime, DataOwnerUserLock lock, Connection connection
            )
    {
        PreparedStatement prestate = null;
        ResultSet result = null;
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
        // 15���O�̓��t
        int[] time = DateEdit.addSec( loginDate, loginTime, -900 );
        int lDate = time[0];
        int lTime = time[1];

        // �����ɂ���Ă����̂�final��t���܂���
        String query = "SELECT * FROM owner_user_log WHERE hotelid = ? AND userid = ?";

        // 15���O�͖{���̏ꍇ
        if ( time[0] == nowDate )
        {
            query += " AND login_date = ?"
                    + " AND login_time BETWEEN ? AND ?";
        }
        // 15���O�͍���̏ꍇ
        else
        {
            query += " AND login_date >= ?"
                    + " AND (login_time >= ? OR login_time <= ?)";
        }
        query += " ORDER BY login_date DESC,login_time DESC"
                + " LIMIT 5 ";

        try
        {
            prestate = connection.prepareStatement( query );
            int i = 1;
            prestate.setString( i++, hotelId );
            prestate.setInt( i++, userId );
            prestate.setInt( i++, lDate );
            prestate.setInt( i++, lTime );
            prestate.setInt( i++, loginTime );
            result = prestate.executeQuery();
            int count = 0;
            while( result.next() )
            {
                if ( result.getInt( "log_level" ) != 1 && result.getInt( "log_level" ) < 100 )
                    count++;
            }
            // �A�����s����5��̏ꍇ�A�L�^
            if ( count == 5 )
            {
                int[] TimesAfterFifteenMin = DateEdit.addSec( nowDate, nowTime, 900 );
                lock.setLockStatus( 1 );
                lock.setLockDate( TimesAfterFifteenMin[0] );
                lock.setLockTime( TimesAfterFifteenMin[1] );
                lock.setMistakeCount( lock.getMistakeCount() + 1 );
                lock.setMistakeDate( nowDate );
                lock.setMistakeTime( nowTime );
                if ( !lock.updateData( hotelId, userId ) )
                {
                    lock.setHotelId( hotelId );
                    lock.setUserId( userId );
                    lock.insertData();
                }
                return false;
            }
            return true;

        }
        catch ( Exception e )
        {
            Logging.error( "[OwnerLoginInfo.isFailOverFiveTimes] Exception=" + e.toString(), e );
            return false;
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
    }
}
