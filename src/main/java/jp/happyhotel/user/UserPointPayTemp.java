/*
 * @(#)UserPointPayTemp.java 1.00 2007/08/23 Copyright (C) ALMEX Inc. 2007 ���[�U�|�C���g�擾�E�X�V�N���X
 */

package jp.happyhotel.user;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelCi;
import jp.happyhotel.data.DataHotelHappie;
import jp.happyhotel.data.DataMasterPoint;
import jp.happyhotel.data.DataUserPointPayTemp;

/**
 * �L�����[�U�[�|�C���g
 * 
 * @author S.Tashiro
 * @version 1.00 2009/08/07
 */
public class UserPointPayTemp implements Serializable
{
    /**
     *
     */
    private static final long      serialVersionUID = 4658546477538933384L;
    private static final int       ONE_HUNDREDTH    = 100;                 // 100����
    private static final int       ADD_FLAG_FINISH  = 1;
    private static final int       TOMORROW         = 1;

    private int                    userPointCount;
    private DataUserPointPayTemp[] userPoint;

    /**
     * �f�[�^�����������܂��B
     */
    public UserPointPayTemp()
    {
        userPointCount = 0;
    }

    public DataUserPointPayTemp[] getUserPoint()
    {
        return userPoint;
    }

    public int getUserPointCount()
    {
        return userPointCount;
    }

    /**
     * ���n�s�[�̃f�[�^���擾����i�����f�̂��̂̂݁j
     * 
     * @param userId ���[�UID
     * @param countNum �擾�����i0�F�S�� ��pageNum�����j
     * @param pageNum �y�[�W�ԍ��i0�`�j
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * @see �J�n���͉���o�^���ȍ~��\��
     */
    public boolean getPointList(String userId, int countNum, int pageNum)
    {
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";

        if ( userId.compareTo( "" ) != 0 )
        {
            query += " WHERE hh_user_point_pay_temp.user_id = ?";
            query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
            query += " AND hh_user_basic.regist_status = 9";
            query += " AND hh_user_basic.del_flag = 0";
            // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
            query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";
            query += " AND hh_user_point_pay_temp.add_flag=0";
        }
        else
        {
            return(false);
        }
        query += " ORDER BY get_date DESC, get_time DESC";
        if ( countNum > 0 )
        {
            query += " LIMIT " + (pageNum * countNum) + "," + countNum;
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
            Logging.info( "[UserPointPayTemp.getPointList] Exception=" + e.toString() );
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
                this.userPoint = new DataUserPointPayTemp[this.userPointCount];
                for( i = 0 ; i < userPointCount ; i++ )
                {
                    userPoint[i] = new DataUserPointPayTemp();
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
            Logging.info( "[UserPointPayTemp.getUserPointSub] Exception=" + e.toString() );
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
     * ���[�U�|�C���g���擾����iID����j
     * 
     * @param userId ���[�UID
     * @param nowMonth �����l�����t���O(true:�����l����)
     * @return �|�C���g��
     */
    public int getNowPoint(String userId, boolean nowMonth)
    {
        int point;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        UserBasicInfo ubi;

        if ( userId.compareTo( "" ) == 0 )
        {
            return(0);
        }

        point = 0;
        // ���[�U���̎擾
        ubi = new UserBasicInfo();
        ubi.getUserBasic( userId );

        if ( nowMonth != false )
        {
            query = "SELECT SUM(hh_user_point_pay_temp.point) FROM hh_user_point_pay_temp, hh_user_basic";
            query += " WHERE hh_user_point_pay_temp.user_id = ?";
            query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id ";
            query += " AND hh_user_basic.regist_status = 9";
            query += " AND hh_user_basic.del_flag = 0";
            query += " AND hh_user_point_pay_temp.add_flag=0";
            // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
            query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";
        }
        else
        {

            // �ŏI�W�v���ȍ~�̂��̂̂ݎ擾����
            query = "SELECT SUM( hh_user_point_pay_temp.point ) FROM hh_user_point_pay_temp, hh_user_basic";
            query += " WHERE hh_user_point_pay_temp.user_id = ?";
            query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id ";
            query += " AND hh_user_basic.regist_status = 9";
            query += " AND hh_user_basic.del_flag = 0";
            query += " AND hh_user_point_pay_temp.add_flag=0";
            // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
            query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";
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
                }
            }

        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPayTemp.getNowPoint] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(point);
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
            Logging.info( "[UserPointPayTemp.getMasterPoint] Exception=" + e.toString() );
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
                // DB�̃N���[�Y����
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // �{���̃|�C���g�擾�������擾
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.get_date = ?";
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPoint] Exception=" + e.toString() );
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
                query = "SELECT  hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPoint] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay_temp.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay_temp.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
            // DB�̃N���[�Y����
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
            Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                // DB�̃N���[�Y����
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // �{���̃|�C���g�擾�������擾
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.get_date = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.regist_status_pay = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay_temp.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay_temp.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
            // DB�̃N���[�Y����
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
            Logging.info( "[UserPointPayTemp.getMasterPointExtString] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.regist_status_pay = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.get_date = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                query += " AND hh_user_point_pay_temp.ext_string = ?";
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExtString] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.regist_status_pay = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                query += " AND hh_user_point_pay_temp.ext_string = ?";
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExtString] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.regist_status_pay = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                query += " AND hh_user_point_pay_temp.ext_string = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay_temp.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay_temp.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
     * @param code �|�C���g�R�[�h
     * @param extCode �t�єԍ�
     * @param dmp �|�C���g�Ǘ��}�X�^�N���X
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private boolean getMasterPointExtNum(String userId, int pointKind, int code, int extCode, DataMasterPoint dmp)
    {
        int checkKind;
        int limitDate;
        int startDate;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ? AND code = ?";

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
            Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                // DB�R�l�N�V�����̃N���[�Y����
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // �{���̃|�C���g�擾�������擾
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.get_date = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay_temp.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay_temp.get_date <= " + Integer.valueOf( DateEdit.getDate( 2 ) );
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
            // DB�̃N���[�Y����
            DBConnection.releaseResources( connection );
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
    public boolean getMasterPointExtNum(String userId, int pointKind, int code, int extCode, DataMasterPoint dmp, int date, int time)
    {
        int checkKind;
        int limitDate;
        int startDate;
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_master_point WHERE kind = ? AND code = ?";

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
            Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                // DB�R�l�N�V�����̃N���[�Y����
                DBConnection.releaseResources( connection );
            }
            else if ( dmp.getLimitFlag() == 1 )
            {
                // �{���̃|�C���g�擾�������擾
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.get_date = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

                ret = true;
                try
                {
                    prestate = connection.prepareStatement( query );
                    if ( prestate != null )
                    {
                        prestate.setString( 1, userId );
                        prestate.setInt( 2, dmp.getCode() );
                        prestate.setInt( 3, pointKind );
                        prestate.setInt( 4, date );
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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
                    startDate = Integer.valueOf( DateEdit.addDay( date, -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 4 )
                {
                    checkKind = 2;
                    limitDate = dmp.getRange();
                    // �f�[�^�̎擾�J�n�����Z�o
                    startDate = Integer.valueOf( DateEdit.addDay( date, -1 * Math.abs( limitDate ) ) );
                }
                else if ( dmp.getLimitFlag() == 5 )
                {
                    checkKind = 1;
                    limitDate = dmp.getRange();
                    // �f�[�^�̎擾�J�n�����Z�o
                    startDate = Integer.valueOf( DateEdit.addMonth( date, -1 * Math.abs( limitDate ) ) );
                }

                // �ߋ��̃|�C���g���l��
                query = "SELECT hh_user_point_pay_temp.* FROM hh_user_point_pay_temp, hh_user_basic";
                query += " WHERE hh_user_point_pay_temp.user_id = ?";
                query += " AND hh_user_point_pay_temp.user_id = hh_user_basic.user_id";
                query += " AND hh_user_basic.regist_status = 9";
                query += " AND hh_user_basic.del_flag = 0";
                query += " AND hh_user_point_pay_temp.code = ?";
                query += " AND hh_user_point_pay_temp.point_kind = ?";
                query += " AND hh_user_point_pay_temp.ext_code = ?";
                if ( startDate > 0 )
                {
                    query += " AND hh_user_point_pay_temp.get_date >= " + startDate;
                }
                query += " AND hh_user_point_pay_temp.get_date <= " + date;
                // �ŏ��̓o�^����hh_user_basic.point_pay_update�ɓo�^���Ă��邽�߁A����ȍ~�̓��t��o�^
                query += " AND hh_user_point_pay_temp.get_date >= hh_user_basic.point_pay_update ";

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
                        result = prestate.executeQuery();
                        if ( result != null )
                        {
                            if ( result.next() != false )
                            {
                                if ( result.getInt( "get_date" ) > 0 )
                                {
                                    // ���X�n�s�[�ǉ����灛���Ԉȓ��Ȃ�true�A����ȊO�Ȃ�false
                                    ret = DateEdit.isValidDate( result.getInt( "get_date" ), result.getInt( "get_time" ), date, time, checkKind, Math.abs( dmp.getRange() ) );
                                    // �����Ԉȓ��͗��X�n�s�[��ǉ������Ȃ��̂ŁAret�̒l���t�ɂ��ă��^�[��
                                    return(!ret);
                                }
                            }
                        }
                    }
                }
                catch ( Exception e )
                {
                    Logging.info( "[UserPointPayTemp.getMasterPointExt] Exception=" + e.toString() );
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
            // DB�̃N���[�Y����
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * �|�C���g����������
     * 
     * @param userId ���[�U�[ID
     * @param pointCode �|�C���g�R�[�h
     * @param formId �t�H�[��ID
     * @param memo ����
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setPoint(String userId, int pointCode, int formId, String memo)
    {
        boolean ret;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;

        dmp = new DataMasterPoint();
        dupt = new DataUserPointPayTemp();
        ret = dmp.getData( pointCode );
        if ( ret == false )
        {
            return false;
        }

        ret = getMasterPointExtNum( userId, dmp.getKind(), pointCode, formId, dmp );
        if ( ret != false )
        {
            try
            {
                dupt = new DataUserPointPayTemp();
                dupt.setUserId( userId );
                dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dupt.setCode( dmp.getCode() );
                // �|�C���g�̉������`�F�b�N
                if ( dmp.getAdjustmentFlag() == 0 )
                {
                    dupt.setPoint( dmp.getAddPoint() );
                }
                else if ( dmp.getAdjustmentFlag() == 1 )
                {
                    dupt.setPoint( -dmp.getAddPoint() );
                }
                dupt.setPointKind( dmp.getKind() );
                dupt.setExtCode( formId );
                dupt.setMemo( memo );
                ret = dupt.insertData();
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPayTemp.setPointPresent ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * ���X�񐔎擾
     * 
     * @param userId ���[�UID
     * @param id �z�e��ID
     * @return ���X��
     */
    public int getMaxVisitSeq(String userId, int id)
    {
        int nMaxSeq = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            // ���X�n�s�[�܂��́A�n�s�[�t�^�����̒���MAX�̂��̂��擾
            connection = DBConnection.getConnection();
            query = "SELECT MAX(visit_seq) AS visit FROM hh_user_point_pay_temp";
            query += " WHERE point_kind BETWEEN 21 AND 23 ";
            query += " AND user_id = ?";
            query += " AND ext_code = ?";
            query += " ORDER BY visit DESC";

            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setString( 1, userId );
                prestate.setInt( 2, id );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        nMaxSeq = result.getInt( 1 );
                    }
                }

            }

        }
        catch ( Exception e )
        {
            Logging.error( "[UserPointPayTemp().getMaxVisitSeq()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(nMaxSeq);
    }

    /**
     * �ő�l�擾
     * 
     * @param userId ���[�UID
     * @return seq
     */
    public int getMaxSeq(String userId)
    {
        int nMaxSeq = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        try
        {
            // ���X�n�s�[�܂��́A�n�s�[�t�^�����̒���MAX�̂��̂��擾
            connection = DBConnection.getConnection();
            query = "SELECT MAX(seq) FROM hh_user_point_pay_temp";
            query += " WHERE user_id = ?";

            prestate = connection.prepareStatement( query );
            if ( prestate != null )
            {
                prestate.setString( 1, userId );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        nMaxSeq = result.getInt( 1 );
                    }
                }

            }

        }
        catch ( Exception e )
        {
            Logging.error( "[UserPointPayTemp().getMaxSeq()] Exception:" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return(nMaxSeq);
    }

    /**
     * �n�s�[���p����ǉ�
     * 
     * @param userId ���[�UID
     * @param code �|�C���g�R�[�h
     * @param extCode �t�єԍ�
     * @param point ���p�|�C���g
     * @param idm �t�F���JID
     * @param userSeq ���[�U�Ǘ��ԍ�
     * @param visitSeq ���X��
     * @param thenPoint ���̓����̃|�C���g
     * @param hotenaviId �z�e�i�rID
     * @param employeeCode �]�ƈ��R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setUsePoint(String userId, int pointCode, int thenPoint, int point, int employeeCode, DataHotelCi dhc)
    {
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;

        dmp = new DataMasterPoint();
        dmp.getData( pointCode );
        Logging.info( "setUsePoint", "pointCode:" + pointCode + ",thenPoint:" + thenPoint + ",usePoint:" + dhc.getUsePoint() );

        ret = this.getMasterPointExtNum( userId, dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {
                // hh_user_point_pay�Ɠ����悤�Ɍ��݃|�C���g����
                // if ( thenPoint >= Math.abs( (dmp.getAddPoint() * point) ) )
                // {
                dupt = new DataUserPointPayTemp();
                dupt.setUserId( userId );
                // �o�^����Ă��Ȃ��ꍇ�̂݃Z�b�g
                if ( dupt.getGetDate() == 0 )
                {
                    dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                dupt.setCode( dmp.getCode() );
                // �|�C���g�̉������`�F�b�N
                if ( dmp.getAdjustmentFlag() == 0 )
                {
                    dupt.setPoint( Math.abs( (dmp.getAddPoint() * point) ) );
                }
                else if ( dmp.getAdjustmentFlag() == 1 )
                {
                    dupt.setPoint( Math.abs( (dmp.getAddPoint() * point) ) * -1 );
                }
                dupt.setPointKind( dmp.getKind() );
                dupt.setExtCode( dhc.getId() );
                dupt.setMemo( "" );
                // �o�^����Ă��Ȃ��ꍇ�̂݃Z�b�g
                if ( dupt.getReflectDate() == 0 )
                {
                    // hh_user_point_pay�ɂ����ǉ����邽�߁A�ǉ��ς݂ɂ���
                    dupt.setReflectDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                }
                dupt.setAddFlag( ADD_FLAG_FINISH );
                dupt.setIdm( dhc.getIdm() );
                dupt.setUserSeq( dhc.getUserSeq() );
                dupt.setVisitSeq( dhc.getVisitSeq() );
                dupt.setRoomNo( dhc.getRoomNo() );
                dupt.setThenPoint( thenPoint );
                dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                dupt.setEmployeeCode( employeeCode );
                // ���[�U�^�C�v���Z�b�g
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );

                ret = dupt.insertData();
                // }
                // else
                // {
                // ret = false;
                // }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPayTemp.setUsePoint] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * �n�s�[���p����ǉ�
     * 
     * @param userId ���[�UID
     * @param code �|�C���g�R�[�h
     * @param extCode �t�єԍ�
     * @param point ���p�|�C���g
     * @param idm �t�F���JID
     * @param userSeq ���[�U�Ǘ��ԍ�
     * @param visitSeq ���X��
     * @param thenPoint ���̓����̃|�C���g
     * @param hotenaviId �z�e�i�rID
     * @param employeeCode �]�ƈ��R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setUsePointUpdate(String userId, int pointCode, int thenPoint, int point, int employeeCode, DataHotelCi dhc)
    {
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;

        dmp = new DataMasterPoint();
        dmp.getData( pointCode );
        Logging.info( "setUsePointUpdate1", "pointCode:" + pointCode + ",thenPoint:" + thenPoint + ",usePoint:" + dhc.getUsePoint() );

        ret = this.getMasterPointExtNum( userId, dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {
                Logging.info( "setUsePointUpdate2", "thenPoint" + thenPoint + "dmp.getAddPoint() :" + dmp.getAddPoint() + ",point:" + point );
                // hh_user_point_pay�Ɠ����悤�Ɍ��݃|�C���g����
                // if ( thenPoint >= Math.abs( (dmp.getAddPoint() * point) ) )
                // {
                dupt = new DataUserPointPayTemp();
                ret = dupt.getData( userId, dmp.getKind(), dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
                Logging.info( "setUsePointUpdate3", "ret:" + ret + "userId:" + userId + ",pointKind:" + dmp.getKind() + ",Id:" + dhc.getId() + ",UserSeq:" + dhc.getUserSeq() + ",VisitSeq:" + dhc.getVisitSeq() );

                dupt.setUserId( userId );
                dupt.setCode( dmp.getCode() );
                // �|�C���g�̉������`�F�b�N
                if ( dmp.getAdjustmentFlag() == 0 )
                {
                    dupt.setPoint( Math.abs( (dmp.getAddPoint() * point) ) );
                }
                else if ( dmp.getAdjustmentFlag() == 1 )
                {
                    dupt.setPoint( Math.abs( (dmp.getAddPoint() * point) ) * -1 );
                }
                dupt.setPointKind( dmp.getKind() );
                dupt.setExtCode( dhc.getId() );
                dupt.setMemo( "" );
                // �o�^����Ă��Ȃ��ꍇ�̂݃Z�b�g
                if ( dupt.getGetDate() == 0 )
                {
                    dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                // �o�^����Ă��Ȃ��ꍇ�̂݃Z�b�g
                if ( dupt.getReflectDate() == 0 )
                {
                    dupt.setReflectDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                }
                dupt.setAddFlag( ADD_FLAG_FINISH );
                dupt.setIdm( dhc.getIdm() );
                dupt.setUserSeq( dhc.getUserSeq() );
                dupt.setVisitSeq( dhc.getVisitSeq() );
                dupt.setRoomNo( dhc.getRoomNo() );
                dupt.setSlipNo( dhc.getSlipNo() );
                dupt.setThenPoint( thenPoint );
                dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                dupt.setEmployeeCode( employeeCode );
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );

                if ( ret != false )
                {
                    ret = dupt.updateData( userId, dupt.getSeq() );
                }
                else
                {
                    ret = dupt.insertData();
                }
                // }
                // else
                // {
                // ret = false;
                // }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPayTemp.setUsePoint] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        return(ret);
    }

    /**
     * �n�s�[���p����ǉ�
     * 
     * @param dhc �`�F�b�N�C���f�[�^
     * @param kind �|�C���g�}�X�^�敪
     * @param employeeCode �]�ƈ��R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean cancelUsePoint(DataHotelCi dhc, int kind, int employeeCode)
    {
        boolean ret = false;
        DataUserPointPayTemp dupt;

        try
        {
            dupt = new DataUserPointPayTemp();

            // �|�C���g�ύX�̃f�[�^���擾
            ret = dupt.getData( dhc.getUserId(), kind, dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
            // �ŐV�̃f�[�^���擾�����ꍇ�Ƀ|�C���g��+�ł���΁A����ς݂Ȃ̂ŏ������Ȃ�
            if ( ret != false && dupt.getPoint() <= 0 )
            {
                dupt.setSeq( 0 );
                dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dupt.setPoint( Math.abs( dupt.getPoint() ) );
                // hh_user_point_pay�ɂ����ǉ����邽�߁A�ǉ��ς݂ɂ���
                dupt.setReflectDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dupt.setAddFlag( ADD_FLAG_FINISH );
                dupt.setEmployeeCode( employeeCode );
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );

                ret = dupt.insertData();
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPayTemp.cancelUsePoint] Exception=" + e.toString() );
        }
        finally
        {
        }
        return(ret);
    }

    /*****
     * ���X�n�s�[�̒ǉ��i�t�^�n�s�[�t�^���ɒǉ��j
     * 
     * @param userId ���[�U�[ID
     * @param pointCode �|�C���g�R�[�h
     * @param thenPoint �����|�C���g
     * @param employeeCode �]�ƈ��R�[�h
     * @param dhc �`�F�b�N�C���f�[�^
     * @return
     */
    public int setVisitPoint(String userId, int pointCode, int thenPoint, int employeeCode, DataHotelCi dhc)
    {
        int addPoint;
        int result;
        int reflectDate;
        boolean ret;
        boolean retReflect;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;
        DataHotelHappie dhh;
        DataHotelBasic dhb;
        UserPointPay upp;

        addPoint = 0;
        result = 0;
        reflectDate = 0;
        dmp = new DataMasterPoint();
        dupt = new DataUserPointPayTemp();
        dhh = new DataHotelHappie();
        upp = new UserPointPay();
        retReflect = false;

        ret = dmp.getData( pointCode );
        if ( ret == false )
        {
            return -1;
        }

        dhb = new DataHotelBasic();
        ret = dhb.getData( dhc.getId() );
        if ( ret == false )
        {
            result = -1;
            return result;
        }
        else
        {
            // TODO
            if ( dhb.getRank() < 3 )
            {
                // result = -1;
                // return result;
            }
        }

        ret = getMasterPointExtNum( userId, dmp.getKind(), pointCode, dhc.getId(), dmp, dhc.getCiDate(), dhc.getCiTime() );
        if ( ret != false )
        {
            // hh_user_point_pay�ɗ��X�n�s�[���ǉ����ꂽ��A�ꎞ�Y�t�e�[�u���ɂ��ǉ�
            if ( upp.setVisitPoint( userId, pointCode, thenPoint, 0, dhc ) > 0 )
            {
                addPoint = dmp.getAddPoint();
                // �����t���O��1�������猸�Z�Ȃ̂Ń}�C�i�X���s��
                if ( dmp.getAdjustmentFlag() == 1 )
                {
                    addPoint = Math.abs( addPoint ) * -1;
                }

                // ��������������疳���̔{����������
                if ( dhc.getUserType() == 1 )
                {
                    addPoint = (int)(addPoint * dmp.getFreeMultiple());
                }

                // ���Z����n�s�[�̔{����hh_hotel_happie����擾
                dhh.getData( dhc.getId() );

                // ���X�n�s�[�̔{�����`�F�b�N
                if ( dhh.getComePointMultiple() > 1 )
                {
                    addPoint = addPoint * dhh.getComePointMultiple();
                }

                try
                {
                    dupt = new DataUserPointPayTemp();
                    boolean ret_dupt = dupt.getData( userId, dmp.getKind(), dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
                    dupt.setUserId( userId );
                    // �o�^����Ă��Ȃ��ꍇ�̂݃Z�b�g
                    if ( dupt.getGetDate() == 0 )
                    {
                        dupt.setGetDate( dhc.getCiDate() );
                        dupt.setGetTime( dhc.getCiTime() );
                    }
                    dupt.setCode( dmp.getCode() );
                    dupt.setPoint( addPoint );
                    dupt.setPointKind( dmp.getKind() );
                    dupt.setExtCode( dhc.getId() );
                    dupt.setMemo( "" );
                    dupt.setAddFlag( ADD_FLAG_FINISH );
                    // �o�^����Ă��Ȃ��ꍇ�̂݃Z�b�g
                    if ( dupt.getReflectDate() == 0 )
                    {
                        dupt.setReflectDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    }
                    dupt.setIdm( dhc.getIdm() );
                    dupt.setUserSeq( dhc.getUserSeq() );
                    dupt.setVisitSeq( dhc.getVisitSeq() );
                    dupt.setThenPoint( thenPoint );
                    dupt.setHotenaviId( dhb.getHotenaviId() );
                    // dupt.setEmployeeCode( employeeCode );
                    dupt.setUserType( dhc.getUserType() );
                    dupt.setCiDate( dhc.getCiDate() );
                    dupt.setCiTime( dhc.getCiTime() );
                    if ( ret_dupt != false )
                    {
                        ret = dupt.updateData( userId, dupt.getSeq() );
                        result = -1;
                    }
                    else
                    {
                        ret = dupt.insertData();
                        if ( ret != false )
                        {
                            result = addPoint;
                        }
                        else
                        {
                            result = -1;
                        }
                    }
                }
                catch ( Exception e )
                {
                    result = -1;
                    Logging.info( "[UserPointPayTemp.setPointPresent ] Exception=" + e.toString() );
                }
                finally
                {
                }
            }
        }
        else
        {
            result = 0;
        }
        return(result);
    }

    /***
     * �z�e�����p�n�s�[�̒ǉ�
     * 
     * @param userId
     * @param pointCode
     * @param price
     * @param employeeCode
     * @param dhc
     * @return
     */
    public int setAmountPoint(String userId, int pointCode, int thenPoint, int price, int employeeCode, DataHotelCi dhc)
    {
        int addPoint;
        int result;
        int reflectDate;
        boolean ret;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;
        DataHotelBasic dhb;

        addPoint = 0;
        result = 0;
        reflectDate = 0;
        dmp = new DataMasterPoint();
        dupt = new DataUserPointPayTemp();
        ret = dmp.getData( pointCode );
        if ( ret == false )
        {
            return -1;
        }

        dhb = new DataHotelBasic();
        ret = dhb.getData( dhc.getId() );
        if ( ret == false )
        {
            result = -1;
            return result;
        }
        else
        {
            // TODO
            if ( dhb.getRank() < 3 )
            {
                // result = -1;
                // return result;
            }
        }

        ret = getMasterPointExtNum( userId, dmp.getKind(), pointCode, dhc.getId(), dmp );
        if ( ret != false )
        {
            addPoint = dmp.getAddPoint();
            // �����ɑ΂���n�s�[�̕t�^���̌v�Z
            if ( price / ONE_HUNDREDTH > 1 )
            {
                if ( dhc.getAmountRate() > 0 )
                {
                    // �{�������Z
                    addPoint = (int)(addPoint * dhc.getAmountRate() * price / ONE_HUNDREDTH);
                }
                else
                {
                    addPoint = addPoint * price / ONE_HUNDREDTH;
                }
            }
            else
            {
                addPoint = 0;
            }

            reflectDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            // �ڍs����1���ȏゾ������ڍs�������Z
            if ( dmp.getShiftDay() > 0 )
            {
                reflectDate = DateEdit.addDay( reflectDate, dmp.getShiftDay() );
            }

            try
            {
                dupt = new DataUserPointPayTemp();
                boolean ret_dupt = dupt.getData( userId, dmp.getKind(), dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
                dupt.setUserId( userId );
                // �o�^����Ă��Ȃ��ꍇ�̂݃Z�b�g
                if ( dupt.getGetDate() == 0 )
                {
                    dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                dupt.setCode( dmp.getCode() );
                dupt.setPoint( addPoint );
                dupt.setPointKind( dmp.getKind() );
                dupt.setExtCode( dhc.getId() );
                dupt.setMemo( "" );
                // �o�^����Ă��Ȃ��ꍇ�̂݃Z�b�g
                if ( dupt.getReflectDate() == 0 )
                {
                    dupt.setReflectDate( reflectDate );
                }
                dupt.setIdm( dhc.getIdm() );
                dupt.setUserSeq( dhc.getUserSeq() );
                dupt.setVisitSeq( dhc.getVisitSeq() );
                dupt.setSlipNo( dhc.getSlipNo() );
                dupt.setRoomNo( dhc.getRoomNo() );
                dupt.setAmount( price );
                dupt.setThenPoint( thenPoint );
                dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                dupt.setEmployeeCode( employeeCode );
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );
                if ( ret_dupt != false )
                {
                    ret = dupt.updateData( userId, dupt.getSeq() );
                    result = -1;
                }
                else
                {
                    ret = dupt.insertData();
                    if ( ret != false )
                    {
                        result = addPoint;
                    }
                    else
                    {
                        result = -1;
                    }
                }

            }
            catch ( Exception e )
            {
                result = -1;
                Logging.info( "[UserPointPayTemp.setAmountPoint(dhc) ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        else
        {
            result = 0;
        }
        return(result);
    }

    /***
     * �z�e�����p�n�s�[�̍X�V
     * 
     * @param userId
     * @param pointCode
     * @param price
     * @param employeeCode
     * @param dhc
     * @return
     */
    public int setAmountPointUpdate(String userId, int pointCode, int thenPoint, int price, int employeeCode, DataHotelCi dhc)
    {
        int addPoint;
        int result;
        int reflectDate;
        boolean ret;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;
        DataHotelBasic dhb;

        addPoint = 0;
        result = 0;
        reflectDate = 0;
        dmp = new DataMasterPoint();
        dupt = new DataUserPointPayTemp();
        ret = dmp.getData( pointCode );
        if ( ret == false )
        {
            return -1;
        }

        dhb = new DataHotelBasic();
        ret = dhb.getData( dhc.getId() );
        if ( ret == false )
        {
            result = -1;
            return result;
        }
        else
        {
            // TODO
            if ( dhb.getRank() < 3 )
            {
                // result = -1;
                // return result;
            }
        }

        ret = getMasterPointExtNum( userId, dmp.getKind(), pointCode, dhc.getId(), dmp );
        if ( ret != false )
        {
            addPoint = dmp.getAddPoint();
            // �����ɑ΂���n�s�[�̕t�^���̌v�Z
            if ( price / ONE_HUNDREDTH > 1 )
            {
                if ( dhc.getAmountRate() > 0 )
                {
                    // �{�������Z
                    addPoint = (int)(addPoint * dhc.getAmountRate() * price / ONE_HUNDREDTH);
                }
                else
                { // �{�������Z
                    addPoint = addPoint * price / ONE_HUNDREDTH;
                }
            }
            else
            {
                addPoint = 0;
            }

            reflectDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            // �ڍs����1���ȏゾ������ڍs�������Z
            if ( dmp.getShiftDay() > 0 )
            {
                reflectDate = DateEdit.addDay( reflectDate, dmp.getShiftDay() );
            }

            try
            {
                dupt = new DataUserPointPayTemp();
                // �C���Ώۂ̃��R�[�h���擾
                ret = dupt.getData( userId, dmp.getKind(), dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );

                dupt.setUserId( userId );
                // �o�^����Ă��Ȃ��ꍇ�̂݃Z�b�g
                if ( dupt.getGetDate() == 0 )
                {
                    dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                dupt.setCode( dmp.getCode() );
                dupt.setPoint( addPoint );
                dupt.setPointKind( dmp.getKind() );
                dupt.setExtCode( dhc.getId() );
                dupt.setMemo( "" );
                // �o�^����Ă��Ȃ��ꍇ�̂݃Z�b�g
                if ( dupt.getReflectDate() == 0 )
                {
                    dupt.setReflectDate( reflectDate );
                }
                dupt.setIdm( dhc.getIdm() );
                dupt.setUserSeq( dhc.getUserSeq() );
                dupt.setVisitSeq( dhc.getVisitSeq() );
                dupt.setSlipNo( dhc.getSlipNo() );
                dupt.setRoomNo( dhc.getRoomNo() );
                dupt.setAmount( price );
                dupt.setThenPoint( thenPoint );
                dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                dupt.setEmployeeCode( employeeCode );
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );

                // �f�[�^�����ꍇ�̓C���T�[�g
                if ( ret == false )
                {
                    ret = dupt.insertData();
                    if ( ret != false )
                    {
                        result = addPoint;
                    }
                    else
                    {
                        result = -1;
                    }
                }
                // ����ꍇ�̓A�b�v�f�[�g
                else
                {
                    ret = dupt.updateData( userId, dupt.getSeq() );
                    result = addPoint;
                }

            }
            catch ( Exception e )
            {
                result = -1;
                Logging.info( "[UserPointPayTemp.setAmountPointUpdate ] Exception=" + e.toString() );
            }
            finally
            {
            }
        }
        else
        {
            result = 0;
        }
        return(result);
    }

    /***
     * �z�e���̗��X�񐔂����߂�
     * 
     * @param userId ���[�UID
     * @param id �z�e��ID
     * @param userSeq ���[�U�Ǘ��ԍ�
     * @param visitSeq ���X��
     * @return
     */
    public boolean isCheckOut(String userId, int id, int getDate, int userSeq, int visitSeq)
    {
        String query = "";
        boolean ret = false;
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        int nVisitSeq = 0;

        // ���łɃz�e��ID�A���[�U�֔ԍ�
        query = "SELECT visit_seq FROM hh_user_point_pay_temp";
        query += " WHERE user_id = ?";
        query += " AND get_date >= ?";
        query += " AND get_date <= ?";
        query += " AND ext_code = ?";
        query += " AND user_seq = ?";
        query += " AND visit_seq = ?";
        query += " AND point_kind = 22";

        try
        {
            // �g�����U�N�V�����̊J�n
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, getDate );
            prestate.setInt( 3, DateEdit.addDay( getDate, TOMORROW ) );
            prestate.setInt( 4, id );
            prestate.setInt( 5, userSeq );
            prestate.setInt( 6, visitSeq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    nVisitSeq = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[UserDataIndex.isCheckOut()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        if ( nVisitSeq > 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

    /**
     * �|�C���g�����擾
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @param pointKind �|�C���g�敪
     * @param userSeq ���[�U�Ǘ��ԍ�
     * @param visitSeq ���X�Ǘ��ԍ�
     * @return
     */
    public boolean getUserPointHistory(String userId, int hotelId, int pointKind, int userSeq, int visitSeq)
    {
        boolean ret;
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int i;
        int count;
        ret = false;

        query = " SELECT * FROM hh_user_point_pay_temp WHERE user_id = ?";
        query += " AND ext_code = ?";
        query += " AND point_kind = ?";
        query += " AND user_seq= ?";
        query += " AND visit_seq = ?";
        query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
        query += " LIMIT 0,1 ";

        try
        {
            // �g�����U�N�V�����̊J�n
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, hotelId );
            prestate.setInt( 3, pointKind );
            prestate.setInt( 4, userSeq );
            prestate.setInt( 5, visitSeq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // ���R�[�h�����擾
                    if ( result.last() != false )
                    {
                        userPointCount = result.getRow();
                    }

                    // �N���X�̔z���p�ӂ��A����������B
                    this.userPoint = new DataUserPointPayTemp[this.userPointCount];
                    for( i = 0 ; i < userPointCount ; i++ )
                    {
                        userPoint[i] = new DataUserPointPayTemp();
                    }

                    count = 0;
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // ���[�U�|�C���g���̎擾
                        this.userPoint[count++].setData( result );
                    }
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[UserDataIndex.getUserPointHistory()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return ret;
    }

    /**
     * �|�C���g�����擾
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @param pointKind �|�C���g�敪
     * @param userSeq ���[�U�Ǘ��ԍ�
     * @param visitSeq ���X�Ǘ��ԍ�
     * @return
     */
    public boolean getUserPointHistory(String userId, int hotelId, int userSeq, int visitSeq)
    {
        boolean ret;
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int i;
        int count;
        ret = false;

        query = " SELECT * FROM hh_user_point_pay_temp WHERE user_id = ?";
        query += " AND ext_code = ?";
        query += " AND user_seq= ?";
        query += " AND visit_seq = ?";
        query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
        query += " LIMIT 0,1 ";

        try
        {
            // �g�����U�N�V�����̊J�n
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, hotelId );
            prestate.setInt( 3, userSeq );
            prestate.setInt( 4, visitSeq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // ���R�[�h�����擾
                    if ( result.last() != false )
                    {
                        userPointCount = result.getRow();
                    }

                    // �N���X�̔z���p�ӂ��A����������B
                    this.userPoint = new DataUserPointPayTemp[this.userPointCount];
                    for( i = 0 ; i < userPointCount ; i++ )
                    {
                        userPoint[i] = new DataUserPointPayTemp();
                    }

                    count = 0;
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // ���[�U�|�C���g���̎擾
                        this.userPoint[count++].setData( result );
                    }
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[UserDataIndex.getUserPointHistory()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return ret;
    }

    /**
     * �|�C���g�����擾
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @param pointKind �|�C���g�敪
     * @param userSeq ���[�U�Ǘ��ԍ�
     * @param visitSeq ���X�Ǘ��ԍ�
     * @return
     */
    public boolean getUserPointHistoryByRsvNo(String userId, int hotelId, int pointKind, String rsvNo)
    {
        boolean ret;
        Connection conn = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        String query;
        int i;
        int count;
        ret = false;

        query = " SELECT * FROM hh_user_point_pay_temp WHERE user_id = ?";
        query += " AND ext_code = ?";
        query += " AND point_kind = ?";
        if ( rsvNo.equals( "" ) == false )
        {
            query += " AND ext_string = ?";
        }
        query += " ORDER BY get_date DESC, get_time DESC, seq DESC";
        query += " LIMIT 0,1 ";

        try
        {
            // �g�����U�N�V�����̊J�n
            conn = DBConnection.getConnection();
            prestate = conn.prepareStatement( query );

            prestate.setString( 1, userId );
            prestate.setInt( 2, hotelId );
            prestate.setInt( 3, pointKind );
            if ( rsvNo.equals( "" ) == false )
            {
                prestate.setString( 4, rsvNo );
            }

            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    // ���R�[�h�����擾
                    if ( result.last() != false )
                    {
                        userPointCount = result.getRow();
                    }

                    // �N���X�̔z���p�ӂ��A����������B
                    this.userPoint = new DataUserPointPayTemp[this.userPointCount];
                    for( i = 0 ; i < userPointCount ; i++ )
                    {
                        userPoint[i] = new DataUserPointPayTemp();
                    }

                    count = 0;
                    result.beforeFirst();
                    while( result.next() != false )
                    {
                        // ���[�U�|�C���g���̎擾
                        this.userPoint[count++].setData( result );
                    }
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[UserDataIndex.getUserPointHistoryByRsvNo()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, conn );
        }

        return ret;
    }

    /**
     * �n�s�[���p����ǉ�
     * 
     * @param dhc �`�F�b�N�C���f�[�^
     * @param kind �|�C���g�}�X�^�敪
     * @param employeeCode �]�ƈ��R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean cancelRsvPoint(DataHotelCi dhc, int kind, int employeeCode)
    {
        boolean ret = false;
        DataUserPointPayTemp dupt;

        try
        {
            dupt = new DataUserPointPayTemp();

            // �|�C���g�ύX�̃f�[�^���擾
            ret = dupt.getData( dhc.getUserId(), kind, dhc.getId(), dhc.getUserSeq(), dhc.getVisitSeq() );
            // �ŐV�̃f�[�^���擾�����ꍇ�Ƀ|�C���g��-�ł���΁A����ς݂Ȃ̂ŏ������Ȃ�
            if ( ret != false && dupt.getPoint() >= 0 )
            {
                dupt.setSeq( 0 );
                dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                dupt.setPoint( Math.abs( dupt.getPoint() ) * -1 );
                // hh_user_point_pay�ɂ����ǉ����邽�߁A�ǉ��ς݂ɂ���
                dupt.setReflectDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dupt.setAddFlag( ADD_FLAG_FINISH );
                dupt.setEmployeeCode( employeeCode );
                dupt.setUserType( dhc.getUserType() );
                dupt.setCiDate( dhc.getCiDate() );
                dupt.setCiTime( dhc.getCiTime() );

                ret = dupt.insertData();
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPayTemp.cancelRsvPoint] Exception=" + e.toString() );
        }
        finally
        {
        }
        return(ret);
    }

    /**
     * �}�C���}��
     * 
     * @param datc
     * @param pointCode
     * @param thenPoint
     * @return
     */
    public int insertMile(DataHotelCi dhc, int pointCode, int thenPoint)
    {
        int nMaxSeq = 0;
        int reflectDate = 0;
        int addFlag = 0;
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;

        dmp = new DataMasterPoint();
        dmp.getData( pointCode );

        ret = this.getMasterPointExtNum( dhc.getUserId(), dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {
                reflectDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                // �ڍs����1���ȏゾ������ڍs�������Z
                if ( dmp.getShiftDay() > 0 )
                {
                    reflectDate = DateEdit.addDay( reflectDate, dmp.getShiftDay() );
                }
                else
                {
                    addFlag = ADD_FLAG_FINISH;
                }

                nMaxSeq = getMaxSeq( dhc.getUserId() );
                // �C���T�[�g�ΏۂƂȂ邽�߁A+1����
                nMaxSeq++;
                // hh_user_point_pay�Ɠ����悤�Ɍ��݃|�C���g����
                if ( thenPoint >= Math.abs( dhc.getUsePoint() ) )
                {
                    dupt = new DataUserPointPayTemp();
                    dupt.setUserId( dhc.getUserId() );
                    dupt.setSeq( nMaxSeq );
                    dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    dupt.setCode( dmp.getCode() );
                    // �|�C���g�̉������`�F�b�N
                    if ( dmp.getAdjustmentFlag() == 0 )
                    {
                        dupt.setPoint( Math.abs( dhc.getUsePoint() ) );
                    }
                    else if ( dmp.getAdjustmentFlag() == 1 )
                    {
                        dupt.setPoint( Math.abs( dhc.getUsePoint() ) * -1 );
                    }
                    dupt.setPointKind( dmp.getKind() );
                    dupt.setExtCode( dhc.getId() );
                    dupt.setMemo( "" );
                    // hh_user_point_pay�ɂ����ǉ����邽�߁A�ǉ��ς݂ɂ���
                    dupt.setReflectDate( reflectDate );
                    dupt.setAddFlag( addFlag );
                    dupt.setIdm( dhc.getIdm() );
                    dupt.setUserSeq( dhc.getUserSeq() );
                    dupt.setVisitSeq( dhc.getVisitSeq() );
                    dupt.setRoomNo( dhc.getRoomNo() );
                    dupt.setThenPoint( thenPoint );
                    dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                    // ���[�U�^�C�v���Z�b�g
                    dupt.setUserType( dhc.getUserType() );
                    dupt.setCiDate( dhc.getCiDate() );
                    dupt.setCiTime( dhc.getCiTime() );
                    ret = dupt.insertDataBySeq();

                }
                else
                {
                    ret = false;
                    nMaxSeq = 0;
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPayTemp.insertMile()] Exception=" + e.toString() );
                nMaxSeq = 0;
            }
            finally
            {
            }
        }
        return(nMaxSeq);
    }

    /**
     * �}�C���X�V
     * 
     * @param datc
     * @param pointCode
     * @param thenPoint
     * @return
     */
    public int updateMile(DataHotelCi dhc, int pointCode, int thenPoint, int seq)
    {
        int reflectDate = 0;
        int addFlag = 0;
        boolean ret = false;
        DataMasterPoint dmp;
        DataUserPointPayTemp dupt;

        dmp = new DataMasterPoint();
        dmp.getData( pointCode );

        ret = this.getMasterPointExtNum( dhc.getUserId(), dmp.getKind(), dmp.getCode(), dhc.getId(), dmp );
        if ( ret != false )
        {
            try
            {
                reflectDate = Integer.parseInt( DateEdit.getDate( 2 ) );
                // �ڍs����1���ȏゾ������ڍs�������Z
                if ( dmp.getShiftDay() > 0 )
                {
                    reflectDate = DateEdit.addDay( reflectDate, dmp.getShiftDay() );
                }
                else
                {
                    addFlag = ADD_FLAG_FINISH;
                }

                // hh_user_point_pay�Ɠ����悤�Ɍ��݃|�C���g����
                if ( thenPoint >= Math.abs( dhc.getUsePoint() ) )
                {
                    dupt = new DataUserPointPayTemp();
                    dupt.getData( dhc.getUserId(), seq );
                    dupt.setUserId( dhc.getUserId() );
                    dupt.setSeq( seq );

                    if ( dupt.getGetDate() == 0 )
                    {
                        dupt.setGetDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        dupt.setGetTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                    }
                    dupt.setCode( dmp.getCode() );
                    // �|�C���g�̉������`�F�b�N
                    if ( dmp.getAdjustmentFlag() == 0 )
                    {
                        dupt.setPoint( Math.abs( dhc.getUsePoint() ) );
                    }
                    else if ( dmp.getAdjustmentFlag() == 1 )
                    {
                        dupt.setPoint( Math.abs( dhc.getUsePoint() ) * -1 );
                    }
                    dupt.setPointKind( dmp.getKind() );
                    dupt.setExtCode( dhc.getId() );
                    dupt.setMemo( "" );
                    // hh_user_point_pay�ɂ����ǉ����邽�߁A�ǉ��ς݂ɂ���
                    if ( dupt.getReflectDate() == 0 )
                    {
                        dupt.setReflectDate( reflectDate );
                    }
                    dupt.setAddFlag( addFlag );
                    dupt.setIdm( dhc.getIdm() );
                    dupt.setUserSeq( dhc.getUserSeq() );
                    dupt.setVisitSeq( dhc.getVisitSeq() );
                    dupt.setRoomNo( dhc.getRoomNo() );
                    dupt.setThenPoint( thenPoint );
                    dupt.setHotenaviId( dhc.getVisitHotenaviId() );
                    // ���[�U�^�C�v���Z�b�g
                    dupt.setUserType( dhc.getUserType() );
                    dupt.setCiDate( dhc.getCiDate() );
                    dupt.setCiTime( dhc.getCiTime() );
                    ret = dupt.updateData( dhc.getUserId(), seq );

                }
                else
                {
                    ret = false;
                    seq = 0;
                }
            }
            catch ( Exception e )
            {
                Logging.info( "[UserPointPayTemp.insertMile()] Exception=" + e.toString() );
                seq = 0;
            }
            finally
            {
            }
        }
        return(seq);
    }

    /**
     * �\��{�[�i�X�}�C���̍X�V
     * 
     * @param userId ���[�UID
     * @param dhc �`�F�b�N�C���f�[�^
     * @return ��������(TRUE:����,FALSE:�ُ�)
     * */
    public boolean setAddBonusMile(String userId, int thenPoint, int employeeCode, DataHotelCi dhc)
    {
        boolean ret = false;
        DataUserPointPayTemp duppt;
        UserPointPay upp;

        try
        {
            duppt = new DataUserPointPayTemp();
            duppt.setUserId( userId );
            if ( duppt.getData( userId, dhc.getRsvNo() ) )
            {
                duppt.setUserSeq( dhc.getUserSeq() );
                duppt.setVisitSeq( dhc.getVisitSeq() );
                duppt.setThenPoint( thenPoint );
                duppt.setEmployeeCode( employeeCode );
                duppt.setAddFlag( ADD_FLAG_FINISH );
                ret = duppt.updateData( userId, duppt.getSeq() ); // ���[�USeq��VisitSeq���X�V
                if ( ret )
                {
                    upp = new UserPointPay();
                    upp.setAddBonusMile( userId, thenPoint, duppt.getPoint(), employeeCode, dhc );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.info( "[UserPointPayTemp.setUsePoint] Exception=" + e.toString() );
        }
        finally
        {
        }
        return(ret);
    }

}
