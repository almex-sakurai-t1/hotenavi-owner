/*
 * @(#)HotelStatus.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 ホテル空室情報取得クラス
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
 * ホテル空室情報取得クラス
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
     * データを初期化します。
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
     * ホテル部屋一覧情報取得
     * 
     * @param hotelId ホテルID
     * @param realTime リアルタイム取得フラグ(1:リアルタイム)
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
                        // データが取得できた場合は、roomInfoの空室、準備中を比較する
                        if ( ret != false )
                        {
                            // 空室、準備中の部屋数が異なる場合はステータスを変更する
                            if ( (roomInfo.RoomEmpty != dhs.getEmpty()) || (roomInfo.RoomClean != dhs.getClean()) )
                            {
                                dhs.setEmpty( roomInfo.RoomEmpty );
                                dhs.setClean( roomInfo.RoomClean );
                                dhs.setLastUpDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                                dhs.setLastUpTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                                dhs.setMode( dhb.getEmptyKind() );

                                // 手動の場合はステータスを更新しない
                                if ( dhb.getEmptyKind() != 1 )
                                {
                                    // ホテル基本情報の更新(hh_hotel_basicとhh_hotel_statusを変更する)
                                    if ( roomInfo.RoomEmpty > 0 )
                                    {
                                        dhs.setEmptyStatus( 1 );
                                    }
                                    else
                                    {
                                        // 準備中を空室に含める場合は再計算
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

                                // hh_hotel_statusを更新または挿入する
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
                        statusMessage = "取得に失敗しました";

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
                // レコード件数取得
                if ( result.last() != false )
                {
                    roomCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelStatus = new DataHotelStatus();
                for( i = 0 ; i < roomCount ; i++ )
                {
                    this.hotelStatus = new DataHotelStatus();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル部屋情報の取得
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
            // 表示内容を編集する
            if ( ret != false )
            {
                // 空室数の計算（準備中の部屋を空室とみなすかどうか）
                emptyRoom = getEmptyRoomCount( this.hotelStatus, dhm );

                // 空室のステータスで満室の場合、または満室のステータスで空室の場合はメッセージを表示しない
                if ( (dhb.getEmptyStatus() == EMPTY && emptyRoom > 0) || (dhb.getEmptyStatus() == FULL && emptyRoom == 0) )
                {
                    // 有無表示
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
                        // 満室で、準備中を表示する場合
                        if ( emptyRoom == 0 && dhm.getCleanDispKind() == 1 )
                        {
                            statusMessage = getCleanMessage( this.hotelStatus, dhm );
                        }
                    }
                    // 指定数
                    else if ( dhm.getEmptyDispType() >= 1 && dhm.getEmptyDispType() < 999 )
                    {
                        // 指定数を表示する
                        if ( emptyRoom >= dhm.getEmptyDispType() )
                        {
                            if ( dhm.getEmptyDispMessage().compareTo( "" ) != 0 )
                            {
                                statusMessage = dhm.getEmptyDispMessage();
                            }
                            else
                            {
                                // 以上表示
                                statusMessage = Integer.toString( dhm.getEmptyDispType() ) + " 室以上";
                            }
                        }
                        else if ( emptyRoom != 0 )
                        {
                            // 満室ではない
                            statusMessage = Integer.toString( emptyRoom ) + " 室";
                        }
                        else
                        {
                            // 満室
                            if ( dhm.getFullDispMessage().compareTo( "" ) != 0 )
                            {
                                statusMessage = dhm.getFullDispMessage();
                            }
                            else
                            {
                                statusMessage = "満室";
                            }
                            // 準備中の部屋表示
                            if ( dhm.getCleanDispKind() == 1 )
                            {
                                statusMessage = getCleanMessage( this.hotelStatus, dhm );
                            }
                        }
                    }
                    else if ( dhm.getEmptyDispType() == 999 )
                    {
                        // 実数表示
                        statusMessage = Integer.toString( emptyRoom ) + " 室";
                        // 満室で、準備中を表示する場合
                        if ( emptyRoom == 0 && dhm.getCleanDispKind() == 1 )
                        {
                            statusMessage = getCleanMessage( this.hotelStatus, dhm );
                        }
                    }
                    else
                    {
                        // 実数表示
                        statusMessage = Integer.toString( emptyRoom ) + " 室";
                        // 満室で、準備中を表示する場合
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
            statusMessage = "取得に失敗しました";
        }
        return(true);
    }

    /**
     * ホテル準備中情報取得
     * 
     * @param dhs DataHotelStatus
     * @param dhm DataHotelMaster
     * @return 処理結果(TRUE:正常,FALSE:異常)
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
                    cleanMessage = "準備中：有";
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
                    cleanMessage = "準備中：" + dhm.getCleanDispType() + "室以上";
                }
                else
                {
                    cleanMessage = "準備中：" + dhs.getClean() + "室";
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
                    cleanMessage = "準備中：" + dhs.getClean() + "室";
                }
            }
        }
        return(cleanMessage);
    }

    /**
     * ホテル空室情報取得
     * 
     * @param dhs HotelStatus
     * @param dhm DataHotelMaster
     * @return 空室数
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
            // 更新対象の値をセットする
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
