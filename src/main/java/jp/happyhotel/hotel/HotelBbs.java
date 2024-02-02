/*
 * @(#)HotelBbs.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 �z�e���N�`�R�~���擾�E�X�V�N���X
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.ReplaceString;
import jp.happyhotel.data.DataHotelBbs;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserBbsVote;

/**
 * �z�e���N�`�R�~���擾�E�X�V�N���X
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 */
public class HotelBbs implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID   = 5929966149311195690L;

    private static final int  HOTELBBS_POINT_MAX = 5;

    private int               m_bbsCount;
    private DataHotelBbs[]    m_hotelBbs;
    private int               m_bbsAllCount;
    private DataUserBasic     m_userBasic;
    private boolean           m_memberFlag;

    /**
     * �f�[�^�����������܂��B
     */
    public HotelBbs()
    {
        m_bbsCount = 0;
        m_bbsAllCount = 0;
        m_memberFlag = false;
    }

    /** �z�e���N�`�R�~��񌏐��擾 **/
    public int getBbsCount()
    {
        return(m_bbsCount);
    }

    /** �z�e���N�`�R�~��񑍌����擾 **/
    public int getBbsAllCount()
    {
        return(m_bbsAllCount);
    }

    /** �z�e���N�`�R�~���擾 **/
    public DataHotelBbs[] getHotelBbs()
    {
        return(m_hotelBbs);
    }

    /** ���[�U��{���擾 **/
    public DataUserBasic getUserBasic()
    {
        return(m_userBasic);
    }

    /** ���[�U��{���擾���� **/
    public boolean isMember()
    {
        return(m_memberFlag);
    }

    /**
     * �z�e���N�`�R�~���擾
     * 
     * @param hotelId �z�e��ID(0: �S��)
     * @param type �\�����@( 0:�f�ڔ��f�Ȃ�,1:�ԐM�Ȃ�,2:�ԐM�Ȃ�(�I�[�i�[),3:���ׂĕ\��(�I�[�i�[), �@4:����(�I�[�i�[),5:����(�I�[�i�[),6:�ԐM���F��,7:�ԐM�ۗ�,8:���ׂĕ\��,9:�ۗ�, 10:�f�ڕs��,11:�V���N�`�R�~�p, 12:�Г��ۗ� )
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
     * @param kindFlag 0:���ׂ�1:�N�`�R�~�̂�2:���ӌ����v�]
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getOwnerBbsList(int hotelId, int type, int countNum, int pageNum, int kindFlag)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // �z�e�������������̂̂ݕ\��
        query = "SELECT * FROM hh_hotel_bbs";
        if ( type == 0 )
        {
            query = query + " WHERE thread_status = 0";
        }
        else if ( type == 1 )
        {
            query = query + " WHERE still_flag < 2";
        }
        else if ( type == 2 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag < 2";
        }
        else if ( type == 3 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 OR thread_status = 5)";
        }
        else if ( type == 4 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag = 0";
        }
        else if ( type == 5 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag = 1";
        }
        else if ( type == 6 )
        {
            query = query + " WHERE ( thread_status = 2 OR thread_status = 3 ) AND still_flag = 3 ";
        }
        else if ( type == 7 )
        {
            query = query + " WHERE ( thread_status = 2 OR thread_status = 3 ) AND still_flag = 4 ";
        }
        else if ( type == 9 )
        {
            query = query + " WHERE thread_status = 3";
        }
        else if ( type == 10 )
        {
            query = query + " WHERE ( thread_status = 4 OR thread_status = 5 )";
        }
        else if ( type == 11 )
        {
            query = query + " WHERE (thread_status = 1 OR thread_status = 2)";
        }
        else if ( type == 12 )
        {
            query = query + " WHERE thread_status = 6";
        }

        if ( hotelId > 0 )
        {
            if ( type >= 0 && type <= 7 )
                query = query + " AND id= ?";
            else if ( type >= 9 && type <= 12 )
                query = query + " AND id= ?";
            else
                query = query + " WHERE id= ?";
        }
        if ( kindFlag == 1 )
        {
            if ( (type >= 0 && type <= 7) || hotelId > 0 )
            {
                query = query + " AND kind_flag=0";
            }
            else if ( (type >= 9 && type <= 12) || hotelId > 0 )
            {
                query = query + " AND kind_flag=0";
            }
            else
            {
                query = query + " WHERE kind_flag=0";
            }
        }
        else if ( kindFlag == 2 )
        {
            if ( (type >= 0 && type <= 7) || hotelId > 0 )
            {
                query = query + " AND kind_flag=1";
            }
            else if ( (type >= 9 && type <= 12) || hotelId > 0 )
            {
                query = query + " AND kind_flag=1";
            }
            else
            {
                query = query + " WHERE kind_flag=1";
            }
        }

        if ( type == 0 || type == 1 || type == 6 || type == 7 || type == 9 )
        {
            query = query + " ORDER BY contribute_date, contribute_time";
        }
        else
        {
            query = query + " ORDER BY contribute_date DESC, contribute_time DESC";
        }

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( hotelId > 0 )
            {
                prestate.setInt( 1, hotelId );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    m_bbsCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.m_hotelBbs = new DataHotelBbs[this.m_bbsCount];
                for( i = 0 ; i < m_bbsCount ; i++ )
                {
                    m_hotelBbs[i] = new DataHotelBbs();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e�����̎擾
                    this.m_hotelBbs[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUnPostBbsList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // �z�e���N�`�R�~�������̎擾
        query = "SELECT COUNT(*) FROM hh_hotel_bbs";
        if ( type == 0 )
        {
            query = query + " WHERE thread_status = 0";
        }
        else if ( type == 1 )
        {
            query = query + " WHERE still_flag < 2";
        }
        else if ( type == 2 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag < 2";
        }
        else if ( type == 3 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 OR thread_status = 5)";
        }
        else if ( type == 4 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag = 0";
        }
        else if ( type == 5 )
        {
            query = query + " WHERE ( thread_status = 1 OR thread_status = 2 OR thread_status = 3 ) AND still_flag = 1";
        }
        else if ( type == 6 )
        {
            query = query + " WHERE ( thread_status=2 OR thread_status=3 ) AND still_flag=3 ";
        }
        else if ( type == 7 )
        {
            query = query + " WHERE ( thread_status=2 OR thread_status=3 ) AND still_flag=4 ";
        }
        else if ( type == 9 )
        {
            query = query + " WHERE thread_status=3";
        }
        else if ( type == 10 )
        {
            query = query + " WHERE ( thread_status=4 OR thread_status=5 )";
        }
        else if ( type == 11 )
        {
            query = query + " WHERE (thread_status=1 OR thread_status=2)";
        }
        else if ( type == 12 )
        {
            query = query + " WHERE thread_status = 6";
        }

        if ( hotelId > 0 )
        {
            if ( type >= 0 && type <= 7 )
                query = query + " AND id= ?";
            else if ( type >= 9 && type <= 12 )
                query = query + " AND id= ?";
            else
                query = query + " WHERE id= ?";
        }
        if ( kindFlag == 1 )
        {
            if ( (type >= 0 && type <= 7) || hotelId > 0 )
            {
                query = query + " AND kind_flag=0";
            }
            else if ( (type >= 9 && type <= 12) || hotelId > 0 )
            {
                query = query + " AND kind_flag=0";
            }
            else
            {
                query = query + " WHERE kind_flag=0";
            }
        }
        else if ( kindFlag == 2 )
        {
            if ( (type >= 0 && type <= 7) || hotelId > 0 )
            {
                query = query + " AND kind_flag=1";
            }
            else if ( (type >= 9 && type <= 12) || hotelId > 0 )
            {
                query = query + " AND kind_flag=1";
            }
            else
            {
                query = query + " WHERE kind_flag=1";
            }
        }

        try
        {
            prestate = connection.prepareStatement( query );
            if ( hotelId > 0 )
            {
                prestate.setInt( 1, hotelId );
            }
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // �������̎擾
                    this.m_bbsAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUnPostBbsHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * �s���{���ʐV���N�`�R�~���擾
     * 
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
     * @param prefId �s���{��ID(0:�S��)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getOwnerBbsListByPref(int countNum, int pageNum, int prefId)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_bbs.* FROM hh_hotel_bbs,hh_hotel_basic";
        query = query + " WHERE ( hh_hotel_bbs.thread_status = 1 OR hh_hotel_bbs.thread_status = 2)";
        query = query + " AND hh_hotel_bbs.kind_flag=0";
        query = query + " AND hh_hotel_basic.id = hh_hotel_bbs.id";
        if ( prefId != 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = " + prefId;
        }
        query = query + " ORDER BY hh_hotel_bbs.contribute_date DESC, hh_hotel_bbs.contribute_time DESC";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    m_bbsCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.m_hotelBbs = new DataHotelBbs[this.m_bbsCount];
                for( i = 0 ; i < m_bbsCount ; i++ )
                {
                    m_hotelBbs[i] = new DataHotelBbs();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e�����̎擾
                    this.m_hotelBbs[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUnPostBbsList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // �z�e���N�`�R�~�������̎擾
        query = "SELECT COUNT(*) FROM hh_hotel_bbs,hh_hotel_basic";
        query = query + " WHERE (thread_status=1 OR thread_status=2)";
        query = query + " AND kind_flag=0";
        query = query + " AND hh_hotel_basic.id = hh_hotel_bbs.id";
        if ( prefId != 0 )
        {
            query = query + " AND hh_hotel_basic.pref_id = " + prefId;
        }

        try
        {
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // �������̎擾
                    this.m_bbsAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUnPostBbsHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * �z�e���N�`�R�~�ꗗ���擾(���t��)
     * 
     * @param hotelId �z�e��ID
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
     * @param kindFlag 0:���ׂ�1:�N�`�R�~�̂�2:���ӌ����v�]
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getBbsList(int hotelId, int countNum, int pageNum, int kindFlag)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // �z�e�������������̂̂ݕ\��
        query = "SELECT * FROM hh_hotel_bbs WHERE id = ? AND (thread_status=1 OR thread_status=2)";
        if ( kindFlag == 1 )
        {
            query = query + " AND kind_flag=0";
        }
        else if ( kindFlag == 2 )
        {
            query = query + " AND kind_flag=1";
        }

        query = query + " ORDER BY seq DESC";

        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    m_bbsCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.m_hotelBbs = new DataHotelBbs[this.m_bbsCount];
                for( i = 0 ; i < m_bbsCount ; i++ )
                {
                    m_hotelBbs[i] = new DataHotelBbs();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e�����̎擾
                    this.m_hotelBbs[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getBbsList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        // �z�e���N�`�R�~�������̎擾
        query = "SELECT COUNT(*) FROM hh_hotel_bbs WHERE id = ? AND (thread_status=1 OR thread_status=2)";
        if ( kindFlag == 1 )
        {
            query = query + " AND kind_flag=0";
        }
        else if ( kindFlag == 2 )
        {
            query = query + " AND kind_flag=1";
        }

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // �������̎擾
                    this.m_bbsAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * �z�e���N�`�R�~���ϓ_���擾
     * 
     * @param hotelId �z�e��ID
     * @return �N�`�R�~���ϓ_(�~100) ���G���[��:-1
     */
    public int getPointAverage(int hotelId)
    {
        int avg;
        int point;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        // �z�e�������������̂̂݃J�E���g����
        query = "SELECT SUM(point*100),COUNT(*) FROM hh_hotel_bbs WHERE id = ? AND (thread_status=1 OR thread_status=2)";
        query = query + " AND kind_flag =0";
        avg = 0;
        point = 0;
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // ���v�_���Ɠ��e�����̎擾
                    point = result.getInt( 1 );
                    count = result.getInt( 2 );
                    // ���ϓ_�v�Z
                    if ( count != 0 )
                    {
                        avg = point / count;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getPointAverage] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(avg);
    }

    /**
     * �z�e���N�`�R�~���ϓ_���擾
     * 
     * @param hotelId �z�e��ID
     * @return �N�`�R�~���ϓ_ ���G���[��:-1
     */
    public BigDecimal getPointAverageByDecimal(int hotelId)
    {
        int avg;
        int point;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        BigDecimal decimalAvg;

        // �z�e�������������̂̂݃J�E���g����
        query = "SELECT SUM(point*100),COUNT(*) FROM hh_hotel_bbs WHERE id = ? AND (thread_status=1 OR thread_status=2)";
        query = query + " AND kind_flag = 0";

        avg = 0;
        point = 0;
        count = 0;
        decimalAvg = new BigDecimal( 1.2 );

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // ���v�_���Ɠ��e�����̎擾
                    point = result.getInt( 1 );
                    count = result.getInt( 2 );
                    // ���ϓ_�v�Z
                    if ( count != 0 )
                    {
                        avg = point / count;
                        decimalAvg = new BigDecimal( Integer.toString( avg ) );
                        decimalAvg = decimalAvg.movePointLeft( 2 );
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getPointAverage] Exception=" + e.toString() );
            return(new BigDecimal( "-1" ));
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(decimalAvg);
    }

    /**
     * �z�e���N�`�R�~���e����
     * 
     * @param hotelId �z�e��ID
     * @param userId ���[�UID
     * @param point �N�`�R�~�]��(0�`5)
     * @param cleanness ���ꂢ���]��(0�`5)
     * @param width �L���]��(0�`5)
     * @param equip �ݔ��]��(0�`5)
     * @param service �T�[�r�X�]��(0�`5)
     * @param cost �R�X�g�p�t�H�[�}���X�]��(0�`5)
     * @param inputData ���̓f�[�^
     * @param kindFlag 0:�N�`�R�~���e�A1:���ӌ����v�]���e
     * @param request HTTP���N�G�X�g
     * @return ��������(0:����,1:�p�����^�ُ�,2:DB�ُ�,3:NG���[�h����,4:���̑�)
     */
    public int setBbsData(int hotelId, String userId, int point, int cleanness, int width, int equip, int service, int cost, String inputData, int kindFlag, HttpServletRequest request)
    {
        boolean ret;
        String userName;
        DataHotelBbs dhb;
        DataUserBasic dub;

        // ���͒l�`�F�b�N
        if ( hotelId <= 0 || userId.compareTo( "" ) == 0 || point > HOTELBBS_POINT_MAX || inputData.compareTo( "" ) == 0 )
        {
            return(1);
        }
        // ���O�̎擾
        dub = new DataUserBasic();
        ret = dub.getData( userId );
        if ( ret != false )
        {
            userName = dub.getHandleName();
        }
        else
        {
            userName = userId;
        }

        // NG���[�h�`�F�b�N

        // ���[�U�����擾����
        // �f�[�^���Z�b�g����
        dhb = new DataHotelBbs();
        dhb.setId( hotelId );
        dhb.setUserId( userId );
        dhb.setUserName( userName );
        dhb.setPoint( point );
        dhb.setMessage( inputData );
        dhb.setReturnOwnerId( "" );
        dhb.setReturnOwnerName( "" );
        dhb.setReturnMessage( "" );
        dhb.setStillFlag( 0 );
        dhb.setThreadStatus( 0 );
        dhb.setOwnerMail( 0 );
        dhb.setUserMail( 0 );
        dhb.setContributeDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dhb.setContributeTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        dhb.setContributeIp( request.getHeader( "X-FORWARDED-FOR" ) != null ? request.getHeader( "X-FORWARDED-FOR" ).split( "," )[0] : request.getRemoteAddr() );
        dhb.setContributeUserAgent( request.getHeader( "user-agent" ) );
        dhb.setReturnDate( 0 );
        dhb.setReturnTime( 0 );
        dhb.setReturnIp( "" );
        dhb.setReturnUserAgent( "" );
        dhb.setCleannessPoint( cleanness );
        dhb.setWidthPoint( width );
        dhb.setEquipPoint( equip );
        dhb.setServicePoint( service );
        dhb.setCostPoint( cost );
        dhb.setKindFlag( kindFlag );
        ret = dhb.insertData();
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
     * ���[�U��{���擾
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getUserData(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_basic WHERE user_id = ? ";

        ret = false;

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
                    m_userBasic = new DataUserBasic();

                    // ���[�U��{���̎擾
                    this.m_userBasic.setData( result );
                    if ( userId.compareTo( m_userBasic.getUserId() ) == 0 )
                        ret = true;
                    else
                        ret = false;
                }
            }
            else
            {
                ret = false;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getUserData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(ret);
    }

    /**
     * �N�`�R�~���[�ς݃`�F�b�N����
     * 
     * @param id �z�e��ID
     * @param seq �Ǘ��ԍ�
     * @param userId ���[�UID
     * @return ��������(TRUE:���[�s��,FALSE:���[��)
     */
    public boolean checkVote(int id, int seq, String userId)
    {
        boolean ret;
        DataUserBbsVote dubv;

        dubv = new DataUserBbsVote();

        // �Ώۃf�[�^�����[�ς݂��`�F�b�N����
        ret = dubv.getData( userId, id, seq );

        return(ret);
    }

    /**
     * �N�`�R�~���[����
     * 
     * @param id �z�e��ID
     * @param seq �Ǘ��ԍ�
     * @param userId ���[�UID
     * @param status ���[��
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean updateVote(int id, int seq, String userId, int status)
    {
        boolean ret;
        DataHotelBbs dhb;
        DataUserBasic dub;
        DataUserBbsVote dubv;

        dubv = new DataUserBbsVote();
        dub = new DataUserBasic();
        dhb = new DataHotelBbs();

        // ���[�U�N�`�R�~���[�����f�[�^�̒ǉ�
        dubv.setUserId( userId );
        dubv.setHotelId( id );
        dubv.setSeq( seq );
        dubv.setVoteDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
        dubv.setVoteTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
        dubv.setVoteStatus( status );
        ret = dubv.insertData();

        // �z�e���N�`�R�~�f�[�^�̓��[���X�V
        ret = dhb.getData( id, seq );
        if ( ret != false )
        {
            dhb.setVoteCount( dhb.getVoteCount() + 1 );
            if ( status == 0 )
            {
                dhb.setVoteYes( dhb.getVoteYes() + 1 );
            }
            dhb.updateData( id, seq );

            // �N�`�R�~���e�҂̃N�`�R�~�|�C���g���i�{�]���j���Z����
            ret = dub.getData( dhb.getUserId() );
            if ( ret != false )
            {
                // �f�[�^���擾����Ă��Ȃ��Ă�true�ŕԂ��Ă��邽�߁A���[�U�f�[�^�̃`�F�b�N���s��
                if ( dub.getUserId().compareTo( dhb.getUserId() ) == 0 )
                {
                    if ( status == 0 )
                    {
                        dub.setBuzzPointPlus( dub.getBuzzPointPlus() + 1 );
                    }
                    else
                    {
                        dub.setBuzzPointMinus( dub.getBuzzPointMinus() + 1 );
                    }
                    ret = dub.updateData( dhb.getUserId() );
                }
            }
        }

        return(true);
    }

    /**
     * �N�`�R�~���e���擾�i�w����t�j
     * 
     * @param userId ���[�U�[ID
     * @param today �擾���t
     * @param kindFlag 0:�N�`�R�~�̂�1:���ӌ����v�]
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public int getTodayBbsList(String userId, int today, int kindFlag)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( userId == null || today < 0 || kindFlag < 0 )
        {
            return(-1);
        }

        // �z�e�������������̂̂ݕ\��
        query = "SELECT COUNT(*) FROM hh_hotel_bbs WHERE user_id = ? ";
        query = query + " AND contribute_date = ?";
        query = query + " AND kind_flag = ?";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, today );
            prestate.setInt( 3, kindFlag );
            result = prestate.executeQuery();

            if ( result != null )
            {
                while( result.next() != false )
                {
                    // �z�e�����̎擾
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getBbsList] Exception=" + e.toString() );
            return(-1);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * ���[�U�[�ʃN�`�R�~�f�[�^�擾
     * 
     * @param userId ���[�UID
     * @param userName �j�b�N�l�[��
     * @param countNum �擾����
     * @param pageNum �y�[�W�ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getBbsListByUserName(String userId, String userName, int countNum, int pageNum)
    {
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        if ( pageNum < 0 )
        {
            pageNum = 0;
        }

        // ���[�U��{�����擾����
        this.m_memberFlag = this.getUserData( userId );

        // �j�b�N�l�[���ŃN�`�R�~�̃f�[�^���擾����
        query = "SELECT hh_hotel_bbs.* FROM hh_hotel_bbs,hh_hotel_basic"
                + " WHERE hh_hotel_bbs.id = hh_hotel_basic.id"
                + " AND hh_hotel_basic.rank = 2"
                + " AND hh_hotel_bbs.user_id = ?"
                + " AND ( hh_hotel_bbs.user_name = ? OR hh_hotel_bbs.user_name = ? )"
                + " AND hh_hotel_bbs.kind_flag = 0"
                + " AND hh_hotel_bbs.thread_status BETWEEN 1 AND 2"
                + " ORDER BY hh_hotel_bbs.contribute_date DESC, hh_hotel_bbs.contribute_time DESC";
        if ( countNum > 0 )
        {
            query += " LIMIT " + (pageNum * countNum) + "," + countNum;
        }

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            // �N���X�T�C�g�X�N���v�e�B���O�΍�ŃG�X�P�[�v����镶�������邽�ߗ����Ń`�F�b�N����
            prestate.setString( 2, ReplaceString.DBEscape( userName ) );
            prestate.setString( 3, ReplaceString.HTMLEscape( userName ) );
            result = prestate.executeQuery();

            if ( result != null )
            {
                // ���R�[�h�����擾
                if ( result.last() != false )
                {
                    m_bbsCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.m_hotelBbs = new DataHotelBbs[this.m_bbsCount];
                for( i = 0 ; i < m_bbsCount ; i++ )
                {
                    m_hotelBbs[i] = new DataHotelBbs();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e�����̎擾
                    this.m_hotelBbs[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getBbsList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            result = null;
            prestate = null;
        }

        query = "SELECT COUNT(*) FROM hh_hotel_bbs,hh_hotel_basic"
                + " WHERE hh_hotel_bbs.id = hh_hotel_basic.id"
                + " AND hh_hotel_basic.rank = 2"
                + " AND hh_hotel_bbs.user_id = ?"
                + " AND ( hh_hotel_bbs.user_name = ? OR hh_hotel_bbs.user_name = ? )"
                + " AND hh_hotel_bbs.kind_flag = 0"
                + " AND hh_hotel_bbs.thread_status BETWEEN 1 AND 2"
                + " ORDER BY hh_hotel_bbs.contribute_date DESC, hh_hotel_bbs.contribute_time DESC";

        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setString( 2, ReplaceString.DBEscape( userName ) );
            prestate.setString( 3, ReplaceString.HTMLEscape( userName ) );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // �������̎擾
                    this.m_bbsAllCount = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[getHotelList] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }
}
