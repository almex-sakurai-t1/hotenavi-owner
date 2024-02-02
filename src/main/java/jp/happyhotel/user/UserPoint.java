/*
 * @(#)UserPoint.java 1.00 2007/08/23 Copyright (C) ALMEX Inc. 2007 ���[�U�|�C���g�擾�E�X�V�N���X
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataUserBasic;
import jp.happyhotel.data.DataUserInvitation;
import jp.happyhotel.data.DataUserPoint;

/**
 * ���[�U�|�C���g�擾�E�X�V�N���X�B ���[�U�̃|�C���g�����擾�E�X�V����@�\��񋟂���
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/31
 * @version 1.1 2007/11/27
 */
public class UserPoint implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 4658546477538933384L;

    private static final int  MYAREA_MAX       = 20;
    private static final int  MYHOTEL_MAX      = 20;

    private int               userPointCount;
    private DataUserPoint[]   userPoint;

    /**
     * �f�[�^�����������܂��B
     */
    public UserPoint()
    {
        userPointCount = 0;
    }

    public DataUserPoint[] getUserPoint()
    {
        return userPoint;
    }

    public int getUserPointCount()
    {
        return userPointCount;
    }

    /**
     * ���[�U�|�C���g���擾����iID����j
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getPointList(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_point";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
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

            ret = getPointListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getPointList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U�|�C���g���擾����iID���祓��t�ŐV����͈�/�������w��j
     * 
     * @param userId ���[�UID
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getPointList(String userId, int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT  * FROM hh_user_point";

        if ( userId.compareTo( "" ) != 0 )
        {
            query = query + " WHERE user_id = ?";
            // 2009�N1��1���ȍ~�̃f�[�^���擾����
            query = query + " AND get_date >= 20090101 ";
        }
        else
        {
            return(false);
        }
        query = query + " ORDER BY get_date DESC, get_time DESC";
        if ( countNum > 0 )
        {
            query = query + " LIMIT " + (pageNum * countNum) + "," + countNum;
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

            ret = getPointListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getPointList] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U�|�C���g���擾����iID����j
     * 
     * @param userId ���[�UID
     * @param nowMonth �����l�����t���O(true:�����l����)
     * @return �|�C���g��
     */
    public int getNowPoint(String userId, boolean nowMonth)
    {
        int point;
        // int pointCollect;
        // String query;
        // Connection connection = null;
        // ResultSet result = null;
        // PreparedStatement prestate = null;
        // UserBasicInfo ubi;
        // UserPointHistory uph;

        if ( userId.compareTo( "" ) == 0 )
        {
            return(0);
        }

        point = 0;
        // pointCollect = 0;

        // �N�������|�C���g�����̌v�Z
        // uph = new UserPointHistory();
        // uph.yearCollectUserPoint( userId );

        // ���݂̃|�C���g�W�v
        // this.collectUserPoint( userId );

        // if ( nowMonth != false )
        // {
        // query = "SELECT SUM(point) FROM hh_user_point";
        // query = query + " WHERE user_id = ?";
        // query = query + " AND get_date >= " + ((Integer.parseInt( DateEdit.getDate( 2 ) ) / 100) * 100);
        // query = query + " AND get_date <= " + ((Integer.parseInt( DateEdit.getDate( 2 ) ) / 100) * 100 + 99);
        // }
        // else
        // {
        // ���[�U���̎擾
        // ubi = new UserBasicInfo();
        // ubi.getUserBasic( userId );

        // pointCollect = ubi.getUserInfo().getPoint();

        // �ŏI�W�v���ȍ~�̂��̂̂ݎ擾����
        // query = "SELECT SUM(point) FROM hh_user_point";
        // query = query + " WHERE user_id = ?";
        // query = query + " AND get_date > " + ubi.getUserInfo().getPointUpdate();
        // }

        // try
        // {
        // connection = DBConnection.getConnection();
        // prestate = connection.prepareStatement( query );

        // if ( userId.compareTo( "" ) != 0 )
        // {
        // prestate.setString( 1, userId );
        // }
        // result = prestate.executeQuery();
        // if ( result != null )
        // {
        // if ( result.next() != false )
        // {
        // point = result.getInt( 1 );

        // if ( nowMonth == false )
        // {
        // point = point + pointCollect;
        // }
        // }
        // }

        // }
        // catch ( Exception e )
        // {
        // Logging.info( "[UserPoint.getNowPoint] Exception=" + e.toString() );
        // }
        // finally
        // {
        // DBConnection.releaseResources( result, prestate, connection );
        // }

        return(point);
    }

    /**
     * ���[�U�|�C���g���擾����i�[���ԍ�����j
     * 
     * @param mobileTermno �[���ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getPointListByTermno(String mobileTermno)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_user_point.* FROM hh_user_point,hh_user_basic";

        if ( mobileTermno.compareTo( "" ) != 0 )
        {
            query = query + " WHERE hh_user_basic.mobile_termno = ?";
            query = query + " AND hh_user_basic.regist_status = 9";
            query = query + " AND hh_user_basic.del_flag = 0";
            query = query + " AND hh_user_basic.user_id = hh_user_point.user_id";
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

            ret = getPointListSub( prestate );

        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getMyHotelListByTermno] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }

        return(ret);
    }

    /**
     * ���[�U�|�C���g�̃f�[�^���Z�b�g
     * 
     * @param prestate �v���y�A�h�X�e�[�g�����g
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getPointListSub(PreparedStatement prestate)
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
                    userPointCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.userPoint = new DataUserPoint[this.userPointCount];
                for( i = 0 ; i < userPointCount ; i++ )
                {
                    userPoint[i] = new DataUserPoint();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ���[�U�|�C���g���̎擾
                    this.userPoint[count++].setData( result );
                }

            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getUserPointSub] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
        }

        if ( userPointCount != 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ����|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointJoin(String userId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // ����|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPoint( userId, 1, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( 0 );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �}�C�G���A�|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointMyArea(String userId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;
        UserMyArea uma;

        ret = false;

        // �}�C�G���A�o�^�����Q�O���𒴂����ꍇ�̓|�C���g���Z���Ȃ�
        uma = new UserMyArea();
        uma.getMyAreaListAll( userId );
        if ( uma.getCount() > MYAREA_MAX )
        {
            return(true);
        }

        // �}�C�G���A�|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPoint( userId, 2, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( 0 );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �}�C�G���A�|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @param jisCode �s�撬���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointMyArea(String userId, int jisCode)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;
        UserMyArea uma;

        ret = false;

        // �}�C�G���A�o�^�����Q�O���𒴂����ꍇ�̓|�C���g���Z���Ȃ�
        uma = new UserMyArea();
        uma.getMyAreaListAll( userId );
        if ( uma.getCount() > MYAREA_MAX )
        {
            return(true);
        }

        // �}�C�G���A�|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 2, jisCode, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( jisCode );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �z�e���ڍ׃|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @param tabNo �^�u�ԍ�(1:��{���,2:����,3:�n�},4:�N�[�|��,5:�N�`�R�~,6:�z�e��HP)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointHotelDetail(String userId, int hotelId, int tabNo)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �z�e���ڍ׃|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 3, hotelId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �X�|���T�[�|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @param sponsorId �X�|���T�[ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointSponsor(String userId, int sponsorId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �X�|���T�[�|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 4, sponsorId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( sponsorId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �����}�K�|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @param magId �����}�KID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointMailmag(String userId, int magId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �����}�K�|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 5, magId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( magId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �}�C�z�e���|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointMyHotel(String userId, int hotelId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;
        UserMyHotel umh;

        ret = false;

        // �}�C�z�e���o�^�����Q�O���𒴂����ꍇ�̓|�C���g���Z���Ȃ�
        umh = new UserMyHotel();
        umh.getMyHotelListAll( userId );
        if ( umh.getCount() > MYHOTEL_MAX )
        {
            return(true);
        }

        // �}�C�z�e���|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 6, hotelId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �N�`�R�~�|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @param bbsId �N�`�R�~ID
     * @param hotelId �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointBbs(String userId, int bbsId, int hotelId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �����}�K�|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointExtString( userId, 7, hotelId, Integer.toString( bbsId ), dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( Integer.toString( bbsId ) );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �����}�K�o�^�|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointMailmagEntry(String userId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �}�C�G���A�|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPoint( userId, 8, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( 0 );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �������߃z�e���|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointHotelRecommend(String userId, int hotelId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �������߃z�e���|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 10, hotelId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �₢���킹�|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @param addPoint ���Z�|�C���g��
     * @param extCode �t�єԍ�
     * @param extString �t�ѕ�����
     * @param personCode �o�^�S����
     * @param appendReason �o�^���R
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointInquiry(String userId, int addPoint, int extCode, String extString, String personCode, String appendReason)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �������߃z�e���|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 11, extCode, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( addPoint );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( extCode );
            dup.setExtString( extString );
            dup.setPersonCode( personCode );
            dup.setAppendReason( appendReason );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �|�C���g�}�X�^�擾����
     * 
     * @param pointKind �|�C���g���
     * @param userId ���[�UID
     * @param dmp �|�C���g�Ǘ��}�X�^�N���X
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getMasterPoint(String userId, int pointKind, DataMasterPoint dmp)
    {
        int checkKind;
        int limitDate;
        int startDate;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ?";

        ret = false;
        checkKind = 0;
        limitDate = 0;
        startDate = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setInt( 1, pointKind );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        dmp.setData( result );
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getMasterPoint] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        // ���̃|�C���g�����Z�Ώۂ��`�F�b�N����
        if ( ret != false )
        {
            ret = false;

            if ( dmp.getLimitFlag() == 0 )
            {
                ret = true;
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // �{���̃|�C���g�擾�������擾
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND get_date = ?";

                ret = true;
                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // �����f�[�^���������ꍇ�͉��Z�ΏۂƂ��Ȃ�
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.getMasterPoint] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            else if ( dmp.getLimitFlag() == 2 )
            {
                // �ߋ��̃|�C���g���擾
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";

                ret = true;
                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // �����f�[�^���������ꍇ�͉��Z�ΏۂƂ��Ȃ�
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.getMasterPoint] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            // ����
            else if ( dmp.getLimitFlag() > 2 && dmp.getLimitFlag() < 6 )
            {
                if ( dmp.getLimitFlag() == 3 )
                {
                    checkKind = 3;
                    // ���Ԃ𑀍삷�郁�\�b�h���Ȃ����ߓ��ɂ��Ɋ��Z
                    limitDate = dmp.getRange() / 24;
                    if ( dmp.getRange() % 24 > 0 )
                    {
                        limitDate++;
                    }
                    // �f�[�^�̎擾�J�n�����Z�o
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 4 )
                {
                    checkKind = 2;
                    limitDate = dmp.getRange();
                    // �f�[�^�̎擾�J�n�����Z�o
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 5 )
                {
                    checkKind = 1;
                    limitDate = dmp.getRange();
                    // �f�[�^�̎擾�J�n�����Z�o
                    startDate = Integer.valueOf( DateEdit.addMonth( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }

                // �ߋ��̃|�C���g���l��
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                if ( startDate > 0 )
                {
                    query += " AND get_date >= " + startDate;
                }
                query += " AND get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );

                query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
                query += " limit 0,1";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                if ( result.getInt( "get_date" ) > 0 )
                                {
                                    // ���X�n�s�[�ǉ����灛���Ԉȓ��Ȃ�true�A����ȊO�Ȃ�false
                                    ret = DateEdit.isValidDate( result.getInt( "get_date" ), result.getInt( "get_time" ), checkKind, Math.abs( dmp.getRange() ) );
                                    // �����Ԉȓ��͗��X�n�s�[��ǉ������Ȃ��̂ŁAret�̒l���t�ɂ��ă��^�[��
                                    return(!ret);
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }

        }
        else
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �|�C���g�}�X�^�擾����(�t�єԍ��Ή��j
     * 
     * @param userId ���[�UID
     * @param pointKind �|�C���g���
     * @param extCode �t�єԍ�
     * @param dmp �|�C���g�Ǘ��}�X�^�N���X
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getMasterPointExt(String userId, int pointKind, int extCode, DataMasterPoint dmp)
    {
        int checkKind;
        int limitDate;
        int startDate;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ?";

        ret = false;
        checkKind = 0;
        limitDate = 0;
        startDate = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setInt( 1, pointKind );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        dmp.setData( result );
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        // ���̃|�C���g�����Z�Ώۂ��`�F�b�N����
        if ( ret != false )
        {
            ret = false;

            if ( dmp.getLimitFlag() == 0 )
            {
                ret = true;
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )// 1��1��
            {
                // �K��false��Ԃ��B20151030.sakurai�@�X���[�N�G���ɂȂ��Ă���̂Ŕ��s�����Ȃ��B
                ret = false;
                DBConnection.releaseResources( connection );

                // �{���̃|�C���g�擾�������擾
                // query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                // query = query + " AND point_kind = ?";
                // query = query + " AND get_date = ?";
                // query = query + " AND ext_code = ?";

                // ret = true;
                // try
                // {
                // prestate = connection.prepareStatement( query );
                // if ( prestate != null )
                // {
                // prestate.setString( 1, userId );
                // prestate.setInt( 2, pointKind );
                // prestate.setInt( 3, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                // prestate.setInt( 4, extCode );
                // result = prestate.executeQuery();
                // if ( result != null )
                // {
                // if ( result.next() != false )
                // {
                // �����f�[�^���������ꍇ�͉��Z�ΏۂƂ��Ȃ�
                // ret = false;
                // }
                // }
                // }
                // }
                // catch ( Exception e )
                // {
                // Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
                // ret = false;
                // }
                // finally
                // {
                // DBConnection.releaseResources( result, prestate, connection );
                // }
            }
            else if ( dmp.getLimitFlag() == 2 )
            {
                // �ߋ��̃|�C���g���擾
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND ext_code = ?";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, pointKind );
                        prestate.setInt( 3, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // �����f�[�^���������ꍇ�͉��Z�ΏۂƂ��Ȃ�
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            // ����
            else if ( dmp.getLimitFlag() > 2 && dmp.getLimitFlag() < 6 )
            {
                if ( dmp.getLimitFlag() == 3 )
                {
                    checkKind = 3;
                    // ���Ԃ𑀍삷�郁�\�b�h���Ȃ����ߓ��ɂ��Ɋ��Z
                    limitDate = dmp.getRange() / 24;
                    if ( dmp.getRange() % 24 > 0 )
                    {
                        limitDate++;
                    }
                    // �f�[�^�̎擾�J�n�����Z�o
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 4 )
                {
                    checkKind = 2;
                    limitDate = dmp.getRange();
                    // �f�[�^�̎擾�J�n�����Z�o
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 5 )
                {
                    checkKind = 1;
                    limitDate = dmp.getRange();
                    // �f�[�^�̎擾�J�n�����Z�o
                    startDate = Integer.valueOf( DateEdit.addMonth( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }

                // �ߋ��̃|�C���g���l��
                query = "SELECT hh_user_point* FROM hh_user_point";
                query += " WHERE user_id = ?";
                query += " AND point_kind = ?";
                query += " AND ext_code = ?";
                if ( startDate > 0 )
                {
                    query += " AND get_date >= " + startDate;
                }
                query += " AND hh_get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
                query += " limit 0,1";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, pointKind );
                        prestate.setInt( 3, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                if ( result.getInt( "get_date" ) > 0 )
                                {
                                    // ���X�n�s�[�ǉ����灛���Ԉȓ��Ȃ�true�A����ȊO�Ȃ�false
                                    ret = DateEdit.isValidDate( result.getInt( "get_date" ), result.getInt( "get_time" ), checkKind, Math.abs( dmp.getRange() ) );
                                    // �����Ԉȓ��͗��X�n�s�[��ǉ������Ȃ��̂ŁAret�̒l���t�ɂ��ă��^�[��
                                    return(!ret);
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }

        }
        else
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �|�C���g�}�X�^�擾����(�t�єԍ��E�t�ѕ����Ή��j
     * 
     * @param pointKind �|�C���g���
     * @param userId ���[�UID
     * @param extCode �t�єԍ�
     * @param dmp �|�C���g�Ǘ��}�X�^�N���X
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getMasterPointExtString(String userId, int pointKind, int extCode, String extString, DataMasterPoint dmp)
    {
        int checkKind;
        int limitDate;
        int startDate;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ?";

        ret = false;
        checkKind = 0;
        limitDate = 0;
        startDate = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setInt( 1, pointKind );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        dmp.setData( result );
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getMasterPointExtString] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        // ���̃|�C���g�����Z�Ώۂ��`�F�b�N����
        if ( ret != false )
        {
            ret = false;

            if ( dmp.getLimitFlag() == 0 )
            {
                ret = true;
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // �{���̃|�C���g�擾�������擾
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND get_date = ?";
                query = query + " AND ext_code = ?";
                query = query + " AND ext_string = ?";

                ret = true;
                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        prestate.setInt( 5, extCode );
                        prestate.setString( 6, extString );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // �����f�[�^���������ꍇ�͉��Z�ΏۂƂ��Ȃ�
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.getMasterPointExtString] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            else if ( dmp.getLimitFlag() == 2 )
            {
                // �ߋ��̃|�C���g���擾
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND ext_code = ?";
                query = query + " AND ext_string = ?";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, extCode );
                        prestate.setString( 5, extString );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // �����f�[�^���������ꍇ�͉��Z�ΏۂƂ��Ȃ�
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.getMasterPointExtString] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            // ����
            else if ( dmp.getLimitFlag() > 2 && dmp.getLimitFlag() < 6 )
            {
                if ( dmp.getLimitFlag() == 3 )
                {
                    checkKind = 3;
                    // ���Ԃ𑀍삷�郁�\�b�h���Ȃ����ߓ��ɂ��Ɋ��Z
                    limitDate = dmp.getRange() / 24;
                    if ( dmp.getRange() % 24 > 0 )
                    {
                        limitDate++;
                    }
                    // �f�[�^�̎擾�J�n�����Z�o
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 4 )
                {
                    checkKind = 2;
                    limitDate = dmp.getRange();
                    // �f�[�^�̎擾�J�n�����Z�o
                    startDate = Integer.valueOf( DateEdit.addDay( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 5 )
                {
                    checkKind = 1;
                    limitDate = dmp.getRange();
                    // �f�[�^�̎擾�J�n�����Z�o
                    startDate = Integer.valueOf( DateEdit.addMonth( Integer.valueOf( DateEdit.getDate( 2 ) ), -1 * Math.abs( limitDate ) ) );
                }

                // �ߋ��̃|�C���g���l��
                query = "SELECT * FROM hh_user_point";
                query += " WHERE user_id = ?";
                query += " AND code = ?";
                query += " AND point_kind = ?";
                query += " AND ext_code = ?";
                query += " AND ext_string = ?";
                if ( startDate > 0 )
                {
                    query += " AND get_date >= " + startDate;
                }
                query += " AND get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );

                query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
                query += " limit 0,1";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, extCode );
                        prestate.setString( 5, extString );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                if ( result.getInt( "get_date" ) > 0 )
                                {
                                    // ���X�n�s�[�ǉ����灛���Ԉȓ��Ȃ�true�A����ȊO�Ȃ�false
                                    ret = DateEdit.isValidDate( result.getInt( "get_date" ), result.getInt( "get_time" ), checkKind, Math.abs( dmp.getRange() ) );
                                    // �����Ԉȓ��͗��X�n�s�[��ǉ������Ȃ��̂ŁAret�̒l���t�ɂ��ă��^�[��
                                    return(!ret);
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }

        }
        else
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �擾�f�R�����m�F����
     * 
     * @param userId ���[�U�[ID
     * @param code �|�C���g�R�[�h
     * @param extCode �f�R���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getDecomeConfirm(String userId, int code, int extCode)
    {
        boolean ret;
        boolean boolGot;
        DataMasterPoint dmp;
        ret = false;

        // �f�R���̃|�C���g���Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointDecome( userId, 12, code, extCode, dmp );
        if ( ret != false )
        {
            boolGot = false;
        }
        else
        {
            boolGot = true;
        }
        return(boolGot);
    }

    /**
     * �f�R���擾�|�C���g�����Z����
     * 
     * @param userId ���[�U�[ID
     * @param code �|�C���g�R�[�h
     * @param extCode �f�R���Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointDecome(String userId, int code, int extCode)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �f�R���̃|�C���g���Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointDecome( userId, 12, code, extCode, dmp );
        Logging.info( "[getMasterPoint]" + ret );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                dup.setPoint( -dmp.getAddPoint() );
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( extCode );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointDecome] Exception=" + e.toString() );
            }
            finally
            {

            }
        }
        return(ret);
    }

    /**
     * �|�C���g�}�X�^�擾����(�t�єԍ��Ή��j
     * 
     * @param userId ���[�UID
     * @param pointKind �|�C���g���
     * @param code �|�C���g�R�[�h
     * @param extCode �t�єԍ�
     * @param dmp �|�C���g�Ǘ��}�X�^�N���X
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getMasterPointDecome(String userId, int pointKind, int code, int extCode, DataMasterPoint dmp)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ? AND code=?";

        ret = false;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setInt( 1, pointKind );
                prestate.setInt( 2, code );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        dmp.setData( result );
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }

        // ���̃|�C���g�����Z�Ώۂ��`�F�b�N����
        if ( ret != false )
        {
            ret = false;

            if ( dmp.getLimitFlag() == 0 )
            {
                ret = true;
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // �{���̃|�C���g�擾�������擾
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND get_date = ?";
                query = query + " AND ext_code = ?";

                ret = true;
                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        prestate.setInt( 5, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // �����f�[�^���������ꍇ�͉��Z�ΏۂƂ��Ȃ�
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
            else if ( dmp.getLimitFlag() == 2 )
            {
                // �ߋ��̃|�C���g���擾
                query = "SELECT * FROM hh_user_point WHERE user_id = ?";
                query = query + " AND code = ?";
                query = query + " AND point_kind = ?";
                query = query + " AND ext_code = ?";

                ret = true;

                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, extCode );
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                // �����f�[�^���������ꍇ�͉��Z�ΏۂƂ��Ȃ�
                                ret = false;
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.getMasterPointExt] Exception=" + e.toString() );
                    ret = false;
                }
                finally
                {
                    DBConnection.releaseResources( result, prestate, connection );
                }
            }
        }
        else
        {
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �Љ�|�C���g�����Z����
     * 
     * @param invitationId �Љ�ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointInvitation(String invitationId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserInvitation dui;
        DataUserPoint dup;

        ret = false;

        dui = new DataUserInvitation();
        dmp = new DataMasterPoint();
        ret = dui.getData( invitationId );
        if ( ret != false )
        {
            if ( dui.getAddFlag() == 0 )
            {

                // �Љ�҂̃|�C���g��ǉ�
                ret = getMasterPoint( dui.getUserId(), 14, dmp );
                if ( ret != false )
                {
                    try
                    {
                        dup = new DataUserPoint();
                        dup.setUserId( dui.getUserId() );
                        dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        dup.setCode( dmp.getCode() );
                        dup.setPoint( dmp.getAddPoint() );
                        dup.setPointKind( dmp.getKind() );
                        ret = dup.insertData();
                    }
                    catch ( Exception e )
                    {
                        Logging.info( "[UserPoint.setPointInvitation ] Exception=" + e.toString() );
                    }
                    finally
                    {
                    }
                }
                // ����҂̃|�C���g��ǉ�
                ret = getMasterPoint( dui.getEntryId(), 14, dmp );
                if ( ret != false )
                {
                    try
                    {
                        dup = new DataUserPoint();
                        dup.setUserId( dui.getEntryId() );
                        dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        dup.setCode( dmp.getCode() );
                        dup.setPoint( dmp.getAddPoint() );
                        dup.setPointKind( dmp.getKind() );
                        ret = dup.insertData();
                    }
                    catch ( Exception e )
                    {
                        Logging.info( "[UserPoint.setPointInvitation ] Exception=" + e.toString() );
                    }
                    finally
                    {
                    }
                }
                return(ret);
            }
            else
            {
                return(false);
            }
        }
        else
        {
            return(false);
        }
    }

    /**
     * ����|�C���g�����Z����
     * 
     * @param userId ���[�U�[ID
     * @param pointCode �|�C���g�R�[�h
     * @param extCode �R�[�h�ԍ�
     * @param applicataionCount �������
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointPresent(String userId, int pointCode, int extCode, int applicationCount)
    {
        int i;
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        dmp = new DataMasterPoint();
        dup = new DataUserPoint();

        i = 0;
        ret = getMasterPointDecome( userId, 13, pointCode, extCode, dmp );
        if ( ret != false )
        {
            for( i = 0 ; i < applicationCount ; i++ )
            {
                try
                {
                    dup = new DataUserPoint();
                    dup.setUserId( userId );
                    dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    dup.setCode( dmp.getCode() );
                    dup.setPoint( -dmp.getAddPoint() );
                    dup.setPointKind( dmp.getKind() );
                    dup.setExtCode( extCode );
                    ret = dup.insertData();
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPoint.setPointPresent ] Exception=" + e.toString() );
                }
                finally
                {
                }
            }
        }
        return(ret);
    }

    /**
     * �N�`�R�~���[�|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @param bbsId �N�`�R�~ID
     * @param hotelId �z�e��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointBbsVote(String userId, int bbsId, int hotelId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �N�`�R�~���[�|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointExtString( userId, 15, hotelId, Integer.toString( bbsId ), dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( Integer.toString( bbsId ) );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �A���P�[�g���e�|�C���g�����Z����
     * 
     * @param userId ���[�UID
     * @param questionId �A���P�[�gID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointEnquete(String userId, int questionId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �A���P�[�g�̉��Z�Ώۃ`�F�b�N�i�j
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 16, questionId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( dmp.getCode() );
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( questionId );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }

    /**
     * �z�e���񋟉���|�C���g�����Z����
     * 
     * @param userId ���[�U�[ID
     * @param pointCode �|�C���g�R�[�h
     * @param extCode �R�[�h�ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointOfferHotelPresent(String userId, int pointCode, int extCode)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        dmp = new DataMasterPoint();
        dup = new DataUserPoint();

        ret = getMasterPointDecome( userId, 17, pointCode, extCode, dmp );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                dup.setPoint( -dmp.getAddPoint() );
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( extCode );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointPresent ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * ����|�C���g�����Z����
     * 
     * @param userId ���[�U�[ID
     * @param pointCode �|�C���g�R�[�h
     * @param formId �t�H�[��ID
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointUserApply(String userId, int pointCode, int formId)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        dmp = new DataMasterPoint();
        dup = new DataUserPoint();
        ret = dmp.getData( pointCode );
        if ( ret == false )
        {
            return false;
        }

        ret = getMasterPointDecome( userId, dmp.getKind(), pointCode, formId, dmp );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                // �|�C���g�̉������`�F�b�N
                if ( dmp.getAdjustmentFlag() == 0 )
                {
                    dup.setPoint( dmp.getAddPoint() );
                }
                else if ( dmp.getAdjustmentFlag() == 1 )
                {
                    dup.setPoint( -dmp.getAddPoint() );
                }
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( formId );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointPresent ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * �����肢�̃|�C���g�������m�F����
     * 
     * @param userId ���[�U�[ID
     * @param code �|�C���g�R�[�h
     * @param loveConstellation ����̐����̊Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getAffinityConfirm(String userId, int loveConstellation)
    {
        boolean ret;
        boolean boolGot;
        DataMasterPoint dmp;
        ret = false;

        dmp = new DataMasterPoint();
        // �|�C���g���Z�ς݂����m�F����
        ret = getMasterPointExt( userId, 19, loveConstellation, dmp );
        if ( ret != false )
        {
            boolGot = false;
        }
        else
        {
            boolGot = true;
        }
        return(boolGot);

    }

    /**
     * �����肢�̎g�p�|�C���g�����Z����
     * 
     * @param userId ���[�U�[ID
     * @param pointCode �|�C���g�R�[�h
     * @param loveConstellation ����̐����̊Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointAffinity(String userId, int loveConstellation)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        dmp = new DataMasterPoint();
        dup = new DataUserPoint();

        ret = getMasterPointExt( userId, 19, loveConstellation, dmp );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                dup.setPoint( -dmp.getAddPoint() );
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( loveConstellation );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointAffinity() ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * �f�R���擾�|�C���g�����Z����
     * 
     * @param userId ���[�U�[ID
     * @param kind �|�C���g�敪
     * @param code �|�C���g�R�[�h
     * @param extCode �f�R���Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointDownload(String userId, int kind, int code, int extCode)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �f�R���̃|�C���g���Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointDecome( userId, kind, code, extCode, dmp );
        Logging.info( "[getMasterPoint]" + ret );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                dup.setPoint( -dmp.getAddPoint() );
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( extCode );
                dup.setAppendReason( "" );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointDownload] Exception=" + e.toString() );
            }
            finally
            {

            }
        }
        return(ret);
    }

    /**
     * �f�R���擾�|�C���g�����Z����
     * 
     * @param userId ���[�U�[ID
     * @param kind �|�C���g�敪
     * @param code �|�C���g�R�[�h
     * @param extCode �f�R���Ǘ��ԍ�
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointDownloadNoPoint(String userId, int kind, int code, int extCode)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;

        ret = false;

        // �f�R���̃|�C���g���Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointDecome( userId, kind, code, extCode, dmp );
        Logging.info( "[getMasterPoint]" + ret );
        if ( ret != false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dup.setCode( dmp.getCode() );
                dup.setPoint( 0 );
                dup.setAppendReason( "�L������̂��߃|�C���g�����" );
                dup.setPointKind( dmp.getKind() );
                dup.setExtCode( extCode );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointDownloadPay] Exception=" + e.toString() );
            }
            finally
            {

            }
        }
        return(ret);
    }

    /**
     * ���Z�E���Z�|�C���g�擾�N���X
     * 
     * @param userId ���[�U�[ID
     * @param startDate �W�v�J�n��
     * @param endDate �W�v�I����
     * @param ���Z�t���O�itrue:�v���X���̂݁Afalse:�}�C�i�X���̂݁j
     * @return ��������(�W�v�|�C���g)
     **/
    public int getUserTotalPointOneSide(String userId, int startDate, int endDate, boolean plusFlag)
    {
        int point;
        // String query;
        // Connection connection = null;
        // ResultSet result = null;
        // PreparedStatement prestate = null;

        point = 0;
        // �w����Ԃ̃|�C���g���擾�i���Z�|�C���g�܂��͌��Z�|�C���g�̂ݎ擾�j
        // query = "SELECT SUM(point) as point_total FROM hh_user_point WHERE user_id= ?";
        // query = query + " AND get_date BETWEEN ? AND ?";
        // if ( plusFlag == false )
        // {
        // query = query + " AND point < 0";
        // }
        // else
        // {
        // query = query + " AND point >= 0";
        // }
        // try
        // {
        // connection = DBConnection.getConnection();
        // prestate = connection.prepareStatement( query );
        // if ( prestate != null )
        // {
        // prestate.setString( 1, userId );

        // prestate.setInt( 2, startDate );
        // prestate.setInt( 3, endDate );
        // result = prestate.executeQuery();
        // if ( result != null )
        // {
        // if ( result.next() != false )
        // {
        // point = result.getInt( 1 );
        // }
        // }
        // }
        // }
        // catch ( Exception e )
        // {
        // Logging.info( "[UserPoint.getUserTotalPointOneSide] Exception=" + e.toString() );
        // }
        // finally
        // {
        // DBConnection.releaseResources( result, prestate, connection );
        // }

        return(point);
    }

    /**
     * �|�C���g�擾�N���X�i���ԁj
     * 
     * @param userId ���[�U�[ID
     * @param startDate �W�v�J�n��
     * @param endDate �W�v�I����
     * @return ��������(�W�v�|�C���g)
     **/
    public int getUserPoint(String userId, int startDate, int endDate)
    {
        int point;
        // String query;
        // Connection connection = null;
        // ResultSet result = null;
        // PreparedStatement prestate = null;

        point = 0;
        // �w����Ԃ̃|�C���g���擾�i���Z�|�C���g�܂��͌��Z�|�C���g�̂ݎ擾�j
        // query = "SELECT SUM(point) as point_total FROM hh_user_point WHERE user_id= ?";
        // query = query + " AND get_date BETWEEN ? AND ?";
        // try
        // {
        // connection = DBConnection.getConnection();
        // prestate = connection.prepareStatement( query );
        // if ( prestate != null )
        // {
        // prestate.setString( 1, userId );
        // prestate.setInt( 2, startDate );
        // prestate.setInt( 3, endDate );
        // result = prestate.executeQuery();
        // if ( result != null )
        // {
        // if ( result.next() != false )
        // {
        // point = result.getInt( 1 );
        // }
        // }
        // }
        // }
        // catch ( Exception e )
        // {
        // Logging.info( "[UserPoint.getUserPoint] Exception=" + e.toString() );
        // }
        // finally
        // {
        // DBConnection.releaseResources( result, prestate, connection );
        // }

        return(point);
    }

    /**
     * �N�������|�C���g�f�[�^�擾
     * 
     * @param userId ���[�UID
     * @param date ���t
     * @return �������ʁitrue:�f�[�^����Afalse:�f�[�^�Ȃ��j
     */
    public boolean getLostPoint(String userId, int date)
    {
        boolean ret;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        String query;
        ret = false;

        query = "SELECT * FROM hh_user_point where user_id = ?" +
                " AND get_date = ?" +
                " AND point_kind = 18";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setString( 1, userId );
                prestate.setInt( 2, date );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getLostPoint] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * �N�������|�C���g�f�[�^�Z�b�g
     * 
     * @param userId
     * @param date
     * @param point
     * @return �������ʁitrue:�ǉ������Afalse:�ǉ����s�j
     * 
     */
    public boolean setLostPoint(String userId, int date, int point)
    {
        final int TIME = 235959;
        final int POINT_CODE = 18;
        final int POINT_KIND = 18;
        boolean ret;
        DataUserPoint dup;

        dup = new DataUserPoint();

        ret = getLostPoint( userId, date );
        if ( ret == false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( date );
                dup.setGetTime( TIME );
                dup.setCode( POINT_CODE );
                dup.setPoint( Math.abs( point ) * -1 );
                dup.setPointKind( POINT_KIND );
                dup.setExtCode( date / 10000 );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointAffinity() ] Exception=" + e.toString() );
                ret = false;
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * �|�C���g�����ꗗ�擾
     * 
     * @param userId
     * @param date
     * @param point
     * @return �������ʁitrue:�ǉ������Afalse:�ǉ����s�j
     * 
     */
    public boolean setPointHistory(String userId, int date, int point)
    {
        final int TIME = 235959;
        final int POINT_CODE = 18;
        final int POINT_KIND = 18;
        boolean ret;
        DataUserPoint dup;

        dup = new DataUserPoint();

        ret = getLostPoint( userId, date );
        if ( ret == false )
        {
            try
            {
                dup = new DataUserPoint();
                dup.setUserId( userId );
                dup.setGetDate( date );
                dup.setGetTime( TIME );
                dup.setCode( POINT_CODE );
                dup.setPoint( Math.abs( point ) * -1 );
                dup.setPointKind( POINT_KIND );
                dup.setExtCode( date / 10000 );
                ret = dup.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPoint.setPointAffinity() ] Exception=" + e.toString() );
                ret = false;
            }
            finally
            {
            }
        }
        return(ret);
    }

    /***
     * ���[�U�|�C���g�f�[�^�W�v����
     * 
     * @param userId
     */
    public void collectUserPoint(String userId)
    {
        boolean ret = false;
        int collectDate;
        int latestPoint = 0;
        DataUserBasic dub;
        dub = new DataUserBasic();

        ret = dub.getData( userId );
        if ( ret != false )
        {
            // ������̂݃|�C���g�W�v�ΏۂƂ���
            if ( dub.getRegistStatus() == 9 && dub.getDelFlag() == 0 )
            {
                // ����̓��t���擾
                collectDate = DateEdit.addDay( Integer.parseInt( DateEdit.getDate( 2 ) ), -1 );
                if ( collectDate > dub.getPointUpdate() )
                {
                    // �|�C���g�X�V���ȍ~�`����܂ł̃|�C���g���擾
                    latestPoint = this.getUserPoint( userId, DateEdit.addDay( dub.getPointUpdate(), 1 ), collectDate );
                    if ( latestPoint != 0 )
                    {
                        // ���v�l���}�C�i�X�ɂȂ�悤��������0���Z�b�g
                        if ( dub.getPoint() + latestPoint > 0 )
                        {
                            dub.setPoint( dub.getPoint() + latestPoint );
                        }
                        else
                        {
                            dub.setPoint( 0 );
                        }
                        dub.setPointUpdate( collectDate );
                        ret = dub.updateData( userId, "�N���X�Ń|�C���g�W�v" );
                        Logging.info( "[UserPoint.collectUserPoint:userID -> " + userId + ", amaountPoint ->" + dub.getPoint() + "]" );
                    }
                }
            }
        }
    }

    /**
     * ���[�U�|�C���g���擾����iID����j
     * 
     * @param userId ���[�UID
     * @param nowMonth �����l�����t���O(true:�����l����)
     * @return �|�C���g��
     */
    public int getNowPointDebug(String userId, boolean nowMonth)
    {
        int point;
        int pointCollect;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        UserBasicInfo ubi;
        UserPointHistory uph;

        if ( userId.compareTo( "" ) == 0 )
        {
            return(0);
        }

        point = 0;
        pointCollect = 0;

        // �N�������|�C���g�����̌v�Z
        uph = new UserPointHistory();
        uph.yearCollectUserPoint( userId );

        // ���݂̃|�C���g�W�v
        this.collectUserPoint( userId );

        if ( nowMonth != false )
        {
            query = "SELECT SUM(point) FROM hh_user_point";
            query = query + " WHERE user_id = ?";
            query = query + " AND get_date >= " + ((Integer.parseInt( DateEdit.getDate( 2 ) ) / 100) * 100);
            query = query + " AND get_date <= " + ((Integer.parseInt( DateEdit.getDate( 2 ) ) / 100) * 100 + 99);
        }
        else
        {
            // ���[�U���̎擾
            ubi = new UserBasicInfo();
            ubi.getUserBasic( userId );

            pointCollect = ubi.getUserInfo().getPoint();

            // �ŏI�W�v���ȍ~�̂��̂̂ݎ擾����
            query = "SELECT SUM(point) FROM hh_user_point";
            query = query + " WHERE user_id = ?";
            query = query + " AND get_date > " + ubi.getUserInfo().getPointUpdate();
        }

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            if ( userId.compareTo( "" ) != 0 )
            {
                prestate.setString( 1, userId );
            }
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    point = result.getInt( 1 );

                    if ( nowMonth == false )
                    {
                        point = point + pointCollect;
                    }
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[UserPoint.getNowPointDebug] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(point);
    }

    /**
     * �z�e���ڍ׃|�C���g�����Z����(API����)
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @param tabNo �^�u�ԍ�(1:��{���,2:����,3:�n�},4:�N�[�|��,5:�N�`�R�~,6:�z�e��HP,20:�A�v��)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPointHotelDetail(String userId, int hotelId)
    {
        final int POINT_CODE_IPHONE = 20;
        boolean ret;
        DataMasterPoint dmp;
        DataUserPoint dup;
        int pointCode = 0;

        ret = false;

        // �z�e���ڍ׃|�C���g�̉��Z�Ώۃ`�F�b�N
        dmp = new DataMasterPoint();
        ret = getMasterPointExt( userId, 3, hotelId, dmp );
        if ( ret != false )
        {
            dup = new DataUserPoint();
            dup.setUserId( userId );
            dup.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dup.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            dup.setCode( pointCode ); // �|�C���g�}�X�^��code���Z�b�g
            dup.setPoint( dmp.getAddPoint() );
            dup.setPointKind( dmp.getKind() );
            dup.setExtCode( hotelId );
            dup.setExtString( "" );
            dup.setPersonCode( "" );
            dup.setAppendReason( "" );
            ret = dup.insertData();
        }

        return(ret);
    }
}
