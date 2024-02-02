/*
 * @(#)DataHotelBbs.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテルクチコミ情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテルクチコミ情報データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/16
 */
public class DataHotelBbs implements Serializable
{
    private static final int  MAX_LENGTH       = 255;
    private static final long serialVersionUID = 2238382223264243248L;
    private int               id;
    private int               seq;
    private String            userId;
    private String            userName;
    private int               point;
    private String            message;
    private String            returnOwnerId;
    private String            returnOwnerName;
    private String            returnMessage;
    private int               stillFlag;
    private int               threadStatus;
    private int               ownerMail;
    private int               userMail;
    private int               contributeDate;
    private int               contributeTime;
    private String            contributeIp;
    private String            contributeUserAgent;
    private int               returnDate;
    private int               returnTime;
    private String            returnIp;
    private String            returnUserAgent;
    private int               cleannessPoint;
    private int               widthPoint;
    private int               equipPoint;
    private int               servicePoint;
    private int               costPoint;
    private int               kindFlag;
    private int               voteCount;
    private int               voteYes;
    private int               lastSendDate;
    private int               returnStillFlag;
    private String            returnAdminId;
    private String            returnAdminName;
    private int               returnAdminDate;
    private int               returnAdminTime;
    private String            returnAdminMessage;
    private String            returnAdminIp;
    private String            returnAdminUserAgent;

    /**
     * データを初期化します。
     */
    public DataHotelBbs()
    {
        id = 0;
        seq = 0;
        userId = "";
        userName = "";
        point = 0;
        message = "";
        returnOwnerId = "";
        returnOwnerName = "";
        returnMessage = "";
        stillFlag = 0;
        threadStatus = 0;
        ownerMail = 0;
        userMail = 0;
        contributeDate = 0;
        contributeTime = 0;
        contributeIp = "";
        contributeUserAgent = "";
        returnDate = 0;
        returnTime = 0;
        returnIp = "";
        returnUserAgent = "";
        cleannessPoint = 0;
        widthPoint = 0;
        equipPoint = 0;
        servicePoint = 0;
        costPoint = 0;
        kindFlag = 0;
        voteCount = 0;
        voteYes = 0;
        lastSendDate = 0;
        returnStillFlag = 0;
        returnAdminId = "";
        returnAdminName = "";
        returnAdminDate = 0;
        returnAdminTime = 0;
        returnAdminMessage = "";
        returnAdminIp = "";
        returnAdminUserAgent = "";
    }

    public int getVoteCount()
    {
        return voteCount;
    }

    public int getVoteYes()
    {
        return voteYes;
    }

    public void setVoteCount(int voteCount)
    {
        this.voteCount = voteCount;
    }

    public void setVoteYes(int voteYes)
    {
        this.voteYes = voteYes;
    }

    public int getCleannessPoint()
    {
        return cleannessPoint;
    }

    public int getContributeDate()
    {
        return contributeDate;
    }

    public String getContributeIp()
    {
        return contributeIp;
    }

    public int getContributeTime()
    {
        return contributeTime;
    }

    public String getContributeUserAgent()
    {
        return contributeUserAgent;
    }

    public int getCostPoint()
    {
        return costPoint;
    }

    public int getEquipPoint()
    {
        return equipPoint;
    }

    public int getId()
    {
        return id;
    }

    public int getKindFlag()
    {
        return kindFlag;
    }

    public int getLastSendDate()
    {
        return lastSendDate;
    }

    public String getMessage()
    {
        return message;
    }

    public int getOwnerMail()
    {
        return ownerMail;
    }

    public int getPoint()
    {
        return point;
    }

    public int getReturnAdminDate()
    {
        return returnAdminDate;
    }

    public String getReturnAdminId()
    {
        return returnAdminId;
    }

    public String getReturnAdminIp()
    {
        return returnAdminIp;
    }

    public String getReturnAdminMessage()
    {
        return returnAdminMessage;
    }

    public String getReturnAdminName()
    {
        return returnAdminName;
    }

    public int getReturnAdminTime()
    {
        return returnAdminTime;
    }

    public String getReturnAdminUserAgent()
    {
        return returnAdminUserAgent;
    }

    public int getReturnDate()
    {
        return returnDate;
    }

    public String getReturnIp()
    {
        return returnIp;
    }

    public String getReturnMessage()
    {
        return returnMessage;
    }

    public String getReturnOwnerId()
    {
        return returnOwnerId;
    }

    public String getReturnOwnerName()
    {
        return returnOwnerName;
    }

    public int getReturnStillFlag()
    {
        return returnStillFlag;
    }

    public int getReturnTime()
    {
        return returnTime;
    }

    public String getReturnUserAgent()
    {
        return returnUserAgent;
    }

    public int getServicePoint()
    {
        return servicePoint;
    }

    public int getSeq()
    {
        return seq;
    }

    public int getStillFlag()
    {
        return stillFlag;
    }

    public int getThreadStatus()
    {
        return threadStatus;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getUserMail()
    {
        return userMail;
    }

    public String getUserName()
    {
        return userName;
    }

    public int getWidthPoint()
    {
        return widthPoint;
    }

    public void setCleannessPoint(int cleannessPoint)
    {
        this.cleannessPoint = cleannessPoint;
    }

    public void setContributeDate(int contributeDate)
    {
        this.contributeDate = contributeDate;
    }

    public void setContributeIp(String contributeIp)
    {
        this.contributeIp = contributeIp;
    }

    public void setContributeTime(int contributeTime)
    {
        this.contributeTime = contributeTime;
    }

    public void setContributeUserAgent(String contributeUserAgent)
    {
        if ( contributeUserAgent == null )
        {
            contributeUserAgent = "";
        }
        else if ( contributeUserAgent.length() > MAX_LENGTH )
        {
            contributeUserAgent = contributeUserAgent.substring( 0, MAX_LENGTH );
        }
        this.contributeUserAgent = contributeUserAgent;

    }

    public void setCostPoint(int costPoint)
    {
        this.costPoint = costPoint;
    }

    public void setEquipPoint(int equipPoint)
    {
        this.equipPoint = equipPoint;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setKindFlag(int kindFlag)
    {
        this.kindFlag = kindFlag;
    }

    public void setLastSendDate(int lastSendDate)
    {
        this.lastSendDate = lastSendDate;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setOwnerMail(int ownerMail)
    {
        this.ownerMail = ownerMail;
    }

    public void setPoint(int point)
    {
        this.point = point;
    }

    public void setReturnAdminDate(int returnAdminDate)
    {
        this.returnAdminDate = returnAdminDate;
    }

    public void setReturnAdminId(String returnAdminId)
    {
        this.returnAdminId = returnAdminId;
    }

    public void setReturnAdminIp(String returnAdminIp)
    {
        this.returnAdminIp = returnAdminIp;
    }

    public void setReturnAdminMessage(String returnAdminMessage)
    {
        this.returnAdminMessage = returnAdminMessage;
    }

    public void setReturnAdminName(String returnAdminName)
    {
        this.returnAdminName = returnAdminName;
    }

    public void setReturnAdminTime(int returnAdminTime)
    {
        this.returnAdminTime = returnAdminTime;
    }

    public void setReturnAdminUserAgent(String returnAdminUserAgent)
    {
        if ( returnAdminUserAgent == null )
        {
            returnAdminUserAgent = "";
        }
        else if ( returnAdminUserAgent.length() > MAX_LENGTH )
        {
            returnAdminUserAgent = returnAdminUserAgent.substring( 0, MAX_LENGTH );
        }
        this.returnAdminUserAgent = returnAdminUserAgent;
    }

    public void setReturnDate(int returnDate)
    {
        this.returnDate = returnDate;
    }

    public void setReturnIp(String returnIp)
    {
        this.returnIp = returnIp;
    }

    public void setReturnMessage(String returnMessage)
    {
        this.returnMessage = returnMessage;
    }

    public void setReturnOwnerId(String returnOwnerId)
    {
        this.returnOwnerId = returnOwnerId;
    }

    public void setReturnOwnerName(String returnOwnerName)
    {
        this.returnOwnerName = returnOwnerName;
    }

    public void setReturnStillFlag(int returnStillFlag)
    {
        this.returnStillFlag = returnStillFlag;
    }

    public void setReturnTime(int returnTime)
    {
        this.returnTime = returnTime;
    }

    public void setReturnUserAgent(String returnUserAgent)
    {
        if ( returnUserAgent == null )
        {
            returnUserAgent = "";
        }
        else if ( returnUserAgent.length() > MAX_LENGTH )
        {
            returnUserAgent = returnUserAgent.substring( 0, MAX_LENGTH );
        }
        this.returnUserAgent = returnUserAgent;
    }

    public void setServicePoint(int servicePoint)
    {
        this.servicePoint = servicePoint;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setStillFlag(int stillFlag)
    {
        this.stillFlag = stillFlag;
    }

    public void setThreadStatus(int threadStatus)
    {
        this.threadStatus = threadStatus;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setUserMail(int userMail)
    {
        this.userMail = userMail;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setWidthPoint(int widthPoint)
    {
        this.widthPoint = widthPoint;
    }

    /**
     * ホテルクチコミ情報データ取得
     * 
     * @param hotelId ホテルコード
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId, int seq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_bbs WHERE id = ? AND seq = ?";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.seq = result.getInt( "seq" );
                    this.userId = result.getString( "user_id" );
                    this.userName = result.getString( "user_name" );
                    this.point = result.getInt( "point" );
                    this.message = result.getString( "message" );
                    this.returnOwnerId = result.getString( "return_owner_id" );
                    this.returnOwnerName = result.getString( "return_owner_name" );
                    this.returnMessage = result.getString( "return_message" );
                    this.stillFlag = result.getInt( "still_flag" );
                    this.threadStatus = result.getInt( "thread_status" );
                    this.ownerMail = result.getInt( "owner_mail" );
                    this.userMail = result.getInt( "user_mail" );
                    this.contributeDate = result.getInt( "contribute_date" );
                    this.contributeTime = result.getInt( "contribute_time" );
                    this.contributeIp = result.getString( "contribute_ip" );
                    this.contributeUserAgent = result.getString( "contribute_useragent" );
                    this.returnDate = result.getInt( "return_date" );
                    this.returnTime = result.getInt( "return_time" );
                    this.returnIp = result.getString( "return_ip" );
                    this.returnUserAgent = result.getString( "return_useragent" );
                    this.cleannessPoint = result.getInt( "cleanness_point" );
                    this.widthPoint = result.getInt( "width_point" );
                    this.equipPoint = result.getInt( "equip_point" );
                    this.servicePoint = result.getInt( "service_point" );
                    this.costPoint = result.getInt( "cost_point" );
                    this.kindFlag = result.getInt( "kind_flag" );
                    this.voteCount = result.getInt( "vote_count" );
                    this.voteYes = result.getInt( "vote_yes" );
                    this.lastSendDate = result.getInt( "last_send_date" );
                    this.returnStillFlag = result.getInt( "return_still_flag" );
                    this.returnAdminId = result.getString( "return_admin_id" );
                    this.returnAdminName = result.getString( "return_admin_name" );
                    this.returnAdminDate = result.getInt( "return_admin_date" );
                    this.returnAdminTime = result.getInt( "return_admin_time" );
                    this.returnAdminMessage = result.getString( "return_admin_message" );
                    this.returnAdminIp = result.getString( "return_admin_ip" );
                    this.returnAdminUserAgent = result.getString( "return_admin_useragent" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBbs.getData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテルクチコミ情報データ設定
     * 
     * @param result ホテル設定情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.seq = result.getInt( "seq" );
                this.userId = result.getString( "user_id" );
                this.userName = result.getString( "user_name" );
                this.point = result.getInt( "point" );
                this.message = result.getString( "message" );
                this.returnOwnerId = result.getString( "return_owner_id" );
                this.returnOwnerName = result.getString( "return_owner_name" );
                this.returnMessage = result.getString( "return_message" );
                this.stillFlag = result.getInt( "still_flag" );
                this.threadStatus = result.getInt( "thread_status" );
                this.ownerMail = result.getInt( "owner_mail" );
                this.userMail = result.getInt( "user_mail" );
                this.contributeDate = result.getInt( "contribute_date" );
                this.contributeTime = result.getInt( "contribute_time" );
                this.contributeIp = result.getString( "contribute_ip" );
                this.contributeUserAgent = result.getString( "contribute_useragent" );
                this.returnDate = result.getInt( "return_date" );
                this.returnTime = result.getInt( "return_time" );
                this.returnIp = result.getString( "return_ip" );
                this.returnUserAgent = result.getString( "return_useragent" );
                this.cleannessPoint = result.getInt( "cleanness_point" );
                this.widthPoint = result.getInt( "width_point" );
                this.equipPoint = result.getInt( "equip_point" );
                this.servicePoint = result.getInt( "service_point" );
                this.costPoint = result.getInt( "cost_point" );
                this.kindFlag = result.getInt( "kind_flag" );
                this.voteCount = result.getInt( "vote_count" );
                this.voteYes = result.getInt( "vote_yes" );
                this.lastSendDate = result.getInt( "last_send_date" );
                this.returnStillFlag = result.getInt( "return_still_flag" );
                this.returnAdminId = result.getString( "return_admin_id" );
                this.returnAdminName = result.getString( "return_admin_name" );
                this.returnAdminDate = result.getInt( "return_admin_date" );
                this.returnAdminTime = result.getInt( "return_admin_time" );
                this.returnAdminMessage = result.getString( "return_admin_message" );
                this.returnAdminIp = result.getString( "return_admin_ip" );
                this.returnAdminUserAgent = result.getString( "return_admin_useragent" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBbs.setData] Exception=" + e.toString() );
        }
        return(ret);
    }

    /**
     * ホテルクチコミ情報データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_hotel_bbs SET ";
        query = query + " id = ?,";
        query = query + " seq = 0,";
        query = query + " user_id = ?,";
        query = query + " user_name = ?,";
        query = query + " point = ?,";
        query = query + " message = ?,";
        query = query + " return_owner_id = ?,";
        query = query + " return_owner_name = ?,";
        query = query + " return_message = ?,";
        query = query + " still_flag = ?,";
        query = query + " thread_status = ?,";
        query = query + " owner_mail = ?,";
        query = query + " user_mail = ?,";
        query = query + " contribute_date = ?,";
        query = query + " contribute_time = ?,";
        query = query + " contribute_ip = ?,";
        query = query + " contribute_useragent = ?,";
        query = query + " return_date = ?,";
        query = query + " return_time = ?,";
        query = query + " return_ip = ?,";
        query = query + " return_useragent = ?,";
        query = query + " cleanness_point = ?,";
        query = query + " width_point = ?,";
        query = query + " equip_point = ?,";
        query = query + " service_point = ?,";
        query = query + " cost_point = ?,";
        query = query + " kind_flag = ?,";
        query = query + " vote_count = ?,";
        query = query + " vote_yes = ?,";
        query = query + " last_send_date = ?,";
        query = query + " return_still_flag = ?,";
        query = query + " return_admin_id = ?,";
        query = query + " return_admin_name = ?,";
        query = query + " return_admin_date = ?,";
        query = query + " return_admin_time = ?,";
        query = query + " return_admin_message = ?,";
        query = query + " return_admin_ip = ?,";
        query = query + " return_admin_useragent = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setString( 2, this.userId );
            prestate.setString( 3, this.userName );
            prestate.setInt( 4, this.point );
            prestate.setString( 5, this.message );
            prestate.setString( 6, this.returnOwnerId );
            prestate.setString( 7, this.returnOwnerName );
            prestate.setString( 8, this.returnMessage );
            prestate.setInt( 9, this.stillFlag );
            prestate.setInt( 10, this.threadStatus );
            prestate.setInt( 11, this.ownerMail );
            prestate.setInt( 12, this.userMail );
            prestate.setInt( 13, this.contributeDate );
            prestate.setInt( 14, this.contributeTime );
            prestate.setString( 15, this.contributeIp );
            prestate.setString( 16, this.contributeUserAgent );
            prestate.setInt( 17, this.returnDate );
            prestate.setInt( 18, this.returnTime );
            prestate.setString( 19, this.returnIp );
            prestate.setString( 20, this.returnUserAgent );
            prestate.setInt( 21, this.cleannessPoint );
            prestate.setInt( 22, this.widthPoint );
            prestate.setInt( 23, this.equipPoint );
            prestate.setInt( 24, this.servicePoint );
            prestate.setInt( 25, this.costPoint );
            prestate.setInt( 26, this.kindFlag );
            prestate.setInt( 27, this.voteCount );
            prestate.setInt( 28, this.voteYes );
            prestate.setInt( 29, this.lastSendDate );
            prestate.setInt( 30, this.returnStillFlag );
            prestate.setString( 31, this.returnAdminId );
            prestate.setString( 32, this.returnAdminName );
            prestate.setInt( 33, this.returnAdminDate );
            prestate.setInt( 34, this.returnAdminTime );
            prestate.setString( 35, this.returnAdminMessage );
            prestate.setString( 36, this.returnAdminIp );
            prestate.setString( 37, this.returnAdminUserAgent );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBbs.insertData] Exception=" + e.toString() );
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
     * ホテルクチコミ情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param seq 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_bbs SET ";
        query = query + " user_id = ?,";
        query = query + " user_name = ?,";
        query = query + " point = ?,";
        query = query + " message = ?,";
        query = query + " return_owner_id = ?,";
        query = query + " return_owner_name = ?,";
        query = query + " return_message = ?,";
        query = query + " still_flag = ?,";
        query = query + " thread_status = ?,";
        query = query + " owner_mail = ?,";
        query = query + " user_mail = ?,";
        query = query + " contribute_date = ?,";
        query = query + " contribute_time = ?,";
        query = query + " contribute_ip = ?,";
        query = query + " contribute_useragent = ?,";
        query = query + " return_date = ?,";
        query = query + " return_time = ?,";
        query = query + " return_ip = ?,";
        query = query + " return_useragent = ?,";
        query = query + " cleanness_point = ?,";
        query = query + " width_point = ?,";
        query = query + " equip_point = ?,";
        query = query + " service_point = ?,";
        query = query + " cost_point = ?,";
        query = query + " kind_flag = ?,";
        query = query + " vote_count = ?,";
        query = query + " vote_yes = ?,";
        query = query + " last_send_date = ?,";
        query = query + " return_still_flag = ?,";
        query = query + " return_admin_id = ?,";
        query = query + " return_admin_name = ?,";
        query = query + " return_admin_date = ?,";
        query = query + " return_admin_time = ?,";
        query = query + " return_admin_message = ?,";
        query = query + " return_admin_ip = ?,";
        query = query + " return_admin_useragent = ?";
        query = query + " WHERE id = ? AND seq = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setString( 2, this.userName );
            prestate.setInt( 3, this.point );
            prestate.setString( 4, this.message );
            prestate.setString( 5, this.returnOwnerId );
            prestate.setString( 6, this.returnOwnerName );
            prestate.setString( 7, this.returnMessage );
            prestate.setInt( 8, this.stillFlag );
            prestate.setInt( 9, this.threadStatus );
            prestate.setInt( 10, this.ownerMail );
            prestate.setInt( 11, this.userMail );
            prestate.setInt( 12, this.contributeDate );
            prestate.setInt( 13, this.contributeTime );
            prestate.setString( 14, this.contributeIp );
            prestate.setString( 15, this.contributeUserAgent );
            prestate.setInt( 16, this.returnDate );
            prestate.setInt( 17, this.returnTime );
            prestate.setString( 18, this.returnIp );
            prestate.setString( 19, this.returnUserAgent );
            prestate.setInt( 20, this.cleannessPoint );
            prestate.setInt( 21, this.widthPoint );
            prestate.setInt( 22, this.equipPoint );
            prestate.setInt( 23, this.servicePoint );
            prestate.setInt( 24, this.costPoint );
            prestate.setInt( 25, this.kindFlag );
            prestate.setInt( 26, this.voteCount );
            prestate.setInt( 27, this.voteYes );
            prestate.setInt( 28, this.lastSendDate );
            prestate.setInt( 29, this.returnStillFlag );
            prestate.setString( 30, this.returnAdminId );
            prestate.setString( 31, this.returnAdminName );
            prestate.setInt( 32, this.returnAdminDate );
            prestate.setInt( 33, this.returnAdminTime );
            prestate.setString( 34, this.returnAdminMessage );
            prestate.setString( 35, this.returnAdminIp );
            prestate.setString( 36, this.returnAdminUserAgent );
            prestate.setInt( 37, id );
            prestate.setInt( 38, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelBbs.updateData] Exception=" + e.toString() );
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
