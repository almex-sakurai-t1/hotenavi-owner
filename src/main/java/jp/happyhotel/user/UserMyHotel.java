/*
 * @(#)UserMyHotel.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ���[�U�}�C�z�e���擾�E�X�V�N���X
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataUserMyHotel;

/**
 * ���[�U�}�C�z�e���擾�E�X�V�N���X�B ���[�U�̃}�C�z�e�������擾�E�X�V����@�\��񋟂���
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/27
 */
public class UserMyHotel implements Serializable
{
    /**
     *
     */
    private static final long    serialVersionUID = 9160510941585039404L;

    private int                  userMyHotelCount;
    private DataUserMyHotel[]    userMyHotel;
    private int                  customMyHotelCount;
    private Map<Integer, String> customMyHotel;

    /**
     * �f�[�^�����������܂��B
     */
    public UserMyHotel()
    {
        userMyHotelCount = 0;
    }

    /** ���[�U�}�C�z�e����񌏐��擾 **/
    public int getCount()
    {
        return(userMyHotelCount);
    }

    /** ���[�U�}�C�z�e�����擾 **/
    public DataUserMyHotel[] getMyHotel()
    {
        return(userMyHotel);
    }

    /** �ڋq�o�^�σ}�C�z�e�����擾 **/
    public Map<Integer, String> getCustomMyHotel()
    {
        return(customMyHotel);
    }

    /**
     * ���[�U�}�C�z�e�����擾����iID����B������͎擾���Ȃ��j
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMyHotelList(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_user_myhotel.* FROM hh_user_myhotel, hh_hotel_basic";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ? AND del_flag = 0";
            query = query + " AND hh_user_myhotel.id = hh_hotel_basic.id";
            query = query + " AND hh_hotel_basic.rank > 0";
            query = query + " ORDER BY append_date DESC, append_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getMyHotelListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getMyHotelList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U�}�C�z�e�����擾����iID����B������͎擾���Ȃ��B�z�e�������o�[�ƕR�t����j
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMyHotelListWithMembers(String userId)
    {
        boolean ret;
        String query = "";
        Connection connection = null;
        PreparedStatement prestate = null;

        if ( userId.compareTo( "" ) != 0 )
        {
            query = "SELECT hh_user_myhotel.* FROM hh_user_myhotel";
            query += " INNER JOIN hh_hotel_basic  ON hh_user_myhotel.id = hh_hotel_basic.id";
            query += "                           AND hh_hotel_basic.rank > 0";
            query += " INNER JOIN ap_hotel_custom ON ap_hotel_custom.id = hh_user_myhotel.id";
            query += "                           AND ap_hotel_custom.user_id = hh_user_myhotel.user_id";
            query += "                           AND ap_hotel_custom.del_flag = 0";
            query += "                           AND ap_hotel_custom.regist_status = 1";
            query += " WHERE hh_user_myhotel.user_id = ?";
            query += "   AND hh_user_myhotel.del_flag = 0";
            query += " ORDER BY hh_user_myhotel.append_date DESC, hh_user_myhotel.append_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getMyHotelListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.error( "[getMyHotelListWithMembers] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U�}�C�z�e�����擾����i�[���ԍ�����j
     * 
     * @param mobileTermno �[���ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMyHotelListByTermno(String mobileTermno)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_user_myhotel.* FROM hh_user_myhotel, hh_user_basic, hh_hotel_basic";

        if ( mobileTermno.compareTo( "" ) != 0 )
        {
            query = query + " WHERE hh_user_basic.mobile_termno = ?";
            query = query + " AND hh_user_basic.regist_status = 9";
            query = query + " AND hh_user_basic.del_flag = 0";
            query = query + " AND hh_user_myhotel.del_flag = 0";
            query = query + " AND hh_user_basic.user_id = hh_user_myhotel.user_id";
            query = query + " AND hh_user_myhotel.id = hh_hotel_basic.id";
            query = query + " AND hh_hotel_basic.rank > 0";
            query = query + " ORDER BY append_date DESC, append_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( mobileTermno.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, mobileTermno );
            }

            ret = getMyHotelListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getMyHotelListByTermno] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U�}�C�z�e�����擾����iID����j�i�폜�ς݂��܂ށj
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getMyHotelListAll(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_myhotel";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
            query = query + " ORDER BY append_date DESC, append_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getMyHotelListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[getMyHotelListAll] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U�}�C�z�e���̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getMyHotelListSub(PreparedStatement prestate)
    {
        int i;
        int count;
        ResultSet result = null;

        count = 0;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    userMyHotelCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.userMyHotel = new DataUserMyHotel[this.userMyHotelCount];
                for( i = 0 ; i < userMyHotelCount ; i++ )
                {
                    userMyHotel[i] = new DataUserMyHotel();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ���[�U���̎擾
                    this.userMyHotel[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getUserMyHotelSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userMyHotelCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ���[�U�}�C�z�e���o�^�m�F����
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @return ��������(true:���łɓo�^�ς�,false:���o�^)
     */
    public boolean existMyHotel(String userId, int hotelId)
    {
        boolean ret;
        DataUserMyHotel dum;

        dum = new DataUserMyHotel();

        // �����f�[�^�̊m�F
        ret = dum.getData( userId, hotelId );
        if ( ret != false )
        {
            if ( dum.getDelFlag() == 1 )
            {
                return(false);
            }
        }

        return(ret);
    }

    /**
     * ���[�U�}�C�z�e���o�^����
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @return ��������(0:����,1:�p�����^�ُ�,2:DB�ُ�,3:���łɓo�^�ς�,4:���̑�)
     */
    public int setMyHotelData(String userId, int hotelId)
    {
        boolean ret;
        DataUserMyHotel dum;

        // ���͒l�`�F�b�N
        if ( hotelId <= 0 || userId.compareTo( "" ) == 0 )
        {
            return(1);
        }

        dum = new DataUserMyHotel();

        // �����f�[�^�̊m�F
        ret = dum.getData( userId, hotelId );
        if ( ret != false )
        {
            if ( dum.getDelFlag() == 1 )
            {
                // �ȑO�폜����Ă����̂ŁA�X�V����B
                dum.setUserId( userId );
                dum.setHotelId( hotelId );
                dum.setDelFlag( 0 );
                dum.setAppendDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dum.setAppendTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dum.setDispDate( 0 );
                dum.setDispTime( 0 );
                ret = dum.updateData( userId, hotelId );
                if ( ret != false )
                {
                    return(0);
                }
                else
                {
                    return(2);
                }
            }

            return(3);
        }

        // �f�[�^���Z�b�g����
        dum.setUserId( userId );
        dum.setHotelId( hotelId );
        dum.setAppendDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dum.setAppendTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        dum.setDispDate( 0 );
        dum.setDispTime( 0 );

        ret = dum.insertData();
        if ( ret != false )
        {
            return(0);
        }
        else
        {
            return(2);
        }
    }

    /**
     * ���[�U�[�}�C�z�e���X�V����
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @param pushFlag push�ʒm�t���O
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updataMyHotelData(String userId, int hotelId, int pushFlag)
    {
        boolean ret;
        DataUserMyHotel dum;

        // ���͒l�`�F�b�N
        if ( hotelId <= 0 || userId.compareTo( "" ) == 0 || (pushFlag < 0 || pushFlag > 1) )
        {
            return(false);
        }

        dum = new DataUserMyHotel();

        // �����f�[�^�̊m�F
        ret = dum.getData( userId, hotelId );
        if ( ret != false )
        {
            // �f�[�^���Z�b�g����
            dum.setUserId( userId );
            dum.setHotelId( hotelId );
            dum.setPushFlag( pushFlag );

            ret = dum.updateData( userId, hotelId );
        }
        return(ret);
    }

    /**
     * ���[�U�}�C�z�e���폜����
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setDeleteMyHotel(String userId, int hotelId)
    {
        boolean ret;
        DataUserMyHotel dum;

        // ���͒l�`�F�b�N
        if ( hotelId <= 0 || userId.compareTo( "" ) == 0 )
        {
            return(false);
        }

        dum = new DataUserMyHotel();

        // �����f�[�^�̊m�F
        ret = dum.getData( userId, hotelId );
        if ( ret != false )
        {
            // �f�[�^���Z�b�g����
            dum.setUserId( userId );
            dum.setHotelId( hotelId );
            dum.setDelFlag( 1 );
            dum.setAppendDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dum.setAppendTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dum.setDispDate( 0 );
            dum.setDispTime( 0 );

            ret = dum.updateData( userId, hotelId );
        }
        return(ret);
    }

    /**
     * �ڋq�o�^�σ}�C�z�e���擾����
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getCustomMyHotelList(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT A.id, C.custom_id FROM hh_user_myhotel A"
                + " INNER JOIN hh_hotel_basic B ON A.id = B.id"
                + " AND B.rank > 0"
                + " LEFT JOIN ap_hotel_custom C ON A.id = C.id"
                + " AND A.user_id = C.user_id"
                + " AND C.regist_status = 1"
                + " AND C.del_flag=0";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE A.user_id = ?";
            query = query + " ORDER BY A.append_date DESC, A.append_time DESC";
        }
        else
        {
            return(false);
        }

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }

            ret = getCustomMyHotelListSub( prestate );
        }
        catch ( Exception e )
        {
            Logging.info( "[getMyHotelList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * �ڋq�o�^�σ}�C�z�e���̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getCustomMyHotelListSub(PreparedStatement prestate)
    {
        int i;
        ResultSet result = null;

        try
        {
            result = prestate.executeQuery();
            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    customMyHotelCount = result.getRow();
                }

                this.customMyHotel = new LinkedHashMap<Integer, String>();

                result.beforeFirst();
                while( result.next() != false )
                {
                    if ( result != null )
                    {
                        customMyHotel.put( result.getInt( "id" ), result.getString( "custom_id" ) );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[getCustomMyHotelListSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( customMyHotelCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }
}
