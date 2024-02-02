/*
 * @(#)HotelStatus.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 �z�e���󎺏��擾�N���X
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;
import jp.happyhotel.data.DataHotelMaster;
import jp.happyhotel.data.DataHotelStatus;

import com.hotenavi2.room.RoomInfo;

/**
 * �z�e���󎺏��擾�N���X
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/26
 */
public class HotelStatus implements Serializable
{
    private static final long serialVersionUID = -2660121573353454312L;
    private static final int  EMPTY            = 1;
    private static final int  FULL             = 2;
    private int               roomCount;
    private DataHotelStatus   hotelStatus;
    private String            statusMessage;

    /**
     * �f�[�^�����������܂��B
     */
    public HotelStatus()
    {
        roomCount = 0;
        statusMessage = "";
    }

    public DataHotelStatus getHotelStatus()
    {
        return hotelStatus;
    }

    public int getRoomCount()
    {
        return roomCount;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public void setHotelStatus(DataHotelStatus hotelStatus)
    {
        this.hotelStatus = hotelStatus;
    }

    public void setRoomCount(int roomCount)
    {
        this.roomCount = roomCount;
    }

    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    /**
     * �z�e�������ꗗ���擾
     * 
     * @param hotelId �z�e��ID
     * @param realTime ���A���^�C���擾�t���O(1:���A���^�C��)
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean getData(int hotelId, int realTime)
    {
        Logging.info( "HotelStatus start" );
        int i;
        int emptyRoom;
        boolean ret;
        boolean retEmpty;
        String query;

        RoomInfo roomInfo;
        DataHotelBasic dhb;
        DataHotelStatus dhs;
        DataHotelMaster dhm;

        emptyRoom = 0;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        roomInfo = new RoomInfo();
        dhb = new DataHotelBasic();
        dhs = new DataHotelStatus();
        dhm = new DataHotelMaster();

        ret = dhm.getData( hotelId );
        ret = dhb.getData( hotelId );

        if ( realTime == 1 )
        {
            retEmpty = false;

            if ( ret != false && dhm.getEmptyDispKind() == 1 )
            {
                Logging.info( "hotenaviId:" + dhb.getEmptyHotenaviId() );
                if ( dhb.getEmptyHotenaviId().compareTo( "" ) != 0 )
                {
                    roomInfo.HotelId = dhb.getEmptyHotenaviId();
                    retEmpty = roomInfo.sendPacket0200();

                    if ( retEmpty != false )
                    {
                        ret = dhs.getData( hotelId );
                        // �f�[�^���擾�ł����ꍇ�́AroomInfo�̋󎺁A���������r����
                        if ( ret != false )
                        {
                            // �󎺁A�������̕��������قȂ�ꍇ�̓X�e�[�^�X��ύX����
                            if ( (roomInfo.RoomEmpty != dhs.getEmpty()) || (roomInfo.RoomClean != dhs.getClean()) )
                            {
                                dhs.setEmpty( roomInfo.RoomEmpty );
                                dhs.setClean( roomInfo.RoomClean );
                                dhs.setLastUpDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                dhs.setLastUpTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                dhs.setMode( dhb.getEmptyKind() );

                                // �蓮�̏ꍇ�̓X�e�[�^�X���X�V���Ȃ�
                                if ( dhb.getEmptyKind() != 1 )
                                {
                                    // �z�e����{���̍X�V(hh_hotel_basic��hh_hotel_status��ύX����)
                                    if ( roomInfo.RoomEmpty > 0 )
                                    {
                                        dhs.setEmptyStatus( 1 );
                                    }
                                    else
                                    {
                                        // ���������󎺂Ɋ܂߂�ꍇ�͍Čv�Z
                                        if ( dhm.getCleanDispKind() == 2 )
                                        {
                                            if ( roomInfo.RoomEmpty + roomInfo.RoomClean > 0 )
                                            {
                                                dhs.setEmptyStatus( 1 );
                                            }
                                            else
                                            {
                                                dhs.setEmptyStatus( 2 );
                                            }
                                        }
                                        else
                                        {
                                            dhs.setEmptyStatus( 2 );
                                        }
                                    }
                                    if ( dhm.getEmptyDispKind() == 0 )
                                    {
                                        dhs.setEmptyStatus( 0 );
                                    }
                                }

                                // hh_hotel_status���X�V�܂��͑}������
                                if ( dhs.getId() != 0 )
                                {
                                    dhs.updateData( hotelId );
                                }
                                else
                                {
                                    dhs.setId( hotelId );
                                    dhs.insertData();
                                }
                            }
                        }

                    }
                    else
                    {
                        statusMessage = "�擾�Ɏ��s���܂���";

                        ret = dhs.getData( hotelId );
                        if ( ret != false )
                        {
                            dhs.setRetryCount( dhs.getRetryCount() + 1 );
                            dhs.updateData( hotelId );
                        }
                    }
                }
            }
            else
            {
                statusMessage = "";

                if ( ret != false )
                {
                    if ( dhm.getEmptyDispKind() == 0 )
                    {
                        dhb.setEmptyStatus( 0 );
                        this.updateHotelBasicStatus( hotelId, dhb.getEmptyStatus() );
                    }
                }
            }
        }
        else
        {
            retEmpty = true;
        }

        query = "SELECT * FROM hh_hotel_status WHERE id = ? ";
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
                    roomCount = result.getRow();
                }

                // �N���X�̔z���p�ӂ��A����������B
                this.hotelStatus = new DataHotelStatus();
                for( i = 0 ; i < roomCount ; i++ )
                {
                    this.hotelStatus = new DataHotelStatus();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // �z�e���������̎擾
                    this.hotelStatus.setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelStatus.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( roomCount > 0 && retEmpty != false )
        {
            // �\�����e��ҏW����
            if ( ret != false )
            {
                // �󎺐��̌v�Z�i�������̕������󎺂Ƃ݂Ȃ����ǂ����j
                emptyRoom = getEmptyRoomCount( this.hotelStatus, dhm );

                // �󎺂̃X�e�[�^�X�Ŗ����̏ꍇ�A�܂��͖����̃X�e�[�^�X�ŋ󎺂̏ꍇ�̓��b�Z�[�W��\�����Ȃ�
                if ( (dhb.getEmptyStatus() == EMPTY && emptyRoom > 0) || (dhb.getEmptyStatus() == FULL && emptyRoom == 0) )
                {
                    // �L���\��
                    if ( dhm.getEmptyDispType() == 0 )
                    {
                        if ( emptyRoom > 0 && dhm.getEmptyDispMessage().compareTo( "" ) != 0 )
                        {
                            statusMessage = dhm.getEmptyDispMessage();
                        }
                        if ( emptyRoom == 0 && dhm.getFullDispMessage().compareTo( "" ) != 0 )
                        {
                            statusMessage = dhm.getFullDispMessage();
                        }
                        // �����ŁA��������\������ꍇ
                        if ( emptyRoom == 0 && dhm.getCleanDispKind() == 1 )
                        {
                            statusMessage = getCleanMessage( this.hotelStatus, dhm );
                        }
                    }
                    // �w�萔
                    else if ( dhm.getEmptyDispType() >= 1 && dhm.getEmptyDispType() < 999 )
                    {
                        // �w�萔��\������
                        if ( emptyRoom >= dhm.getEmptyDispType() )
                        {
                            if ( dhm.getEmptyDispMessage().compareTo( "" ) != 0 )
                            {
                                statusMessage = dhm.getEmptyDispMessage();
                            }
                            else
                            {
                                // �ȏ�\��
                                statusMessage = Integer.toString( dhm.getEmptyDispType() ) + " ���ȏ�";
                            }
                        }
                        else if ( emptyRoom != 0 )
                        {
                            // �����ł͂Ȃ�
                            statusMessage = Integer.toString( emptyRoom ) + " ��";
                        }
                        else
                        {
                            // ����
                            if ( dhm.getFullDispMessage().compareTo( "" ) != 0 )
                            {
                                statusMessage = dhm.getFullDispMessage();
                            }
                            else
                            {
                                statusMessage = "����";
                            }
                            // �������̕����\��
                            if ( dhm.getCleanDispKind() == 1 )
                            {
                                statusMessage = getCleanMessage( this.hotelStatus, dhm );
                            }
                        }
                    }
                    else if ( dhm.getEmptyDispType() == 999 )
                    {
                        // �����\��
                        statusMessage = Integer.toString( emptyRoom ) + " ��";
                        // �����ŁA��������\������ꍇ
                        if ( emptyRoom == 0 && dhm.getCleanDispKind() == 1 )
                        {
                            statusMessage = getCleanMessage( this.hotelStatus, dhm );
                        }
                    }
                    else
                    {
                        // �����\��
                        statusMessage = Integer.toString( emptyRoom ) + " ��";
                        // �����ŁA��������\������ꍇ
                        if ( emptyRoom == 0 && dhm.getCleanDispKind() == 1 )
                        {
                            statusMessage = getCleanMessage( this.hotelStatus, dhm );
                        }
                    }
                }
                else
                {
                    statusMessage = "";
                }
            }
        }
        else
        {
            statusMessage = "�擾�Ɏ��s���܂���";
        }
        return(true);
    }

    /**
     * �z�e�����������擾
     * 
     * @param dhs DataHotelStatus
     * @param dhm DataHotelMaster
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    private String getCleanMessage(DataHotelStatus dhs, DataHotelMaster dhm)
    {
        String cleanMessage;

        cleanMessage = "";
        if ( dhm.getCleanDispKind() == 1 )
        {
            if ( dhm.getCleanDispType() == 0 )
            {
                if ( dhs.getClean() > 0 )
                    cleanMessage = "�������F�L";
                else
                    cleanMessage = "";
            }
            else if ( dhm.getCleanDispType() > 1 && dhm.getCleanDispType() < 999 )
            {
                if ( dhs.getClean() == 0 )
                {
                    cleanMessage = "";
                }
                else if ( dhs.getClean() >= dhm.getCleanDispType() )
                {
                    cleanMessage = "�������F" + dhm.getCleanDispType() + "���ȏ�";
                }
                else
                {
                    cleanMessage = "�������F" + dhs.getClean() + "��";
                }
            }
            else if ( dhm.getCleanDispType() == 999 )
            {
                if ( dhs.getClean() == 0 )
                {
                    cleanMessage = "";
                }
                else if ( dhs.getClean() > 0 )
                {
                    cleanMessage = "�������F" + dhs.getClean() + "��";
                }
            }
        }
        return(cleanMessage);
    }

    /**
     * �z�e���󎺏��擾
     * 
     * @param dhs HotelStatus
     * @param dhm DataHotelMaster
     * @return �󎺐�
     */
    private int getEmptyRoomCount(DataHotelStatus dhs, DataHotelMaster dhm)
    {
        int emptyCount;

        emptyCount = 0;
        if ( dhm.getCleanDispKind() == 2 )
        {
            emptyCount = dhs.getEmpty() + dhs.getClean();
        }
        else
        {
            emptyCount = dhs.getEmpty();
        }

        return(emptyCount);
    }

    private boolean updateHotelBasicStatus(int id, int status)
    {
        int result;
        boolean ret;
        String query;

        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "UPDATE hh_hotel_status SET ";
        query = query + " empty_status = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?";
        query = query + " WHERE id = ?";

        try
        {
            Logging.info( "[HotelStatus.updateHotelBasicStatus]" + query + ", id=" + id + ", emptyStatus=" + status );
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( 1, status );
            prestate.setInt( 2, Integer.parseInt(DateEdit.getDate(2)) );
            prestate.setInt( 3, Integer.parseInt(DateEdit.getTime(1)) );
            prestate.setInt( 4, id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelStatus.updateHotelBasicStatus] Exception=" + e.toString() );
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
