/*
 * @(#)DataUserInvitation.java 1.00 2008/04/02 Copyright (C) ALMEX Inc. 2007 ユーザー友達紹介データ取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ポイント管理マスタ取得クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2008/04/02
 */
public class DataUserInvitation implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -8097095864270004870L;

    private String            invitationId;
    private String            userId;
    private int               registDate;
    private int               registTime;
    private String            entryId;
    private String            mailAddr;
    private int               entryDate;
    private int               entryTime;
    private int               addFlag;

    public DataUserInvitation()
    {
        invitationId = "";
        userId = "";
        registDate = 0;
        registTime = 0;
        entryId = "";
        mailAddr = "";
        entryDate = 0;
        entryTime = 0;
        addFlag = 0;
    }

    public int getAddFlag()
    {
        return addFlag;
    }

    public int getEntryDate()
    {
        return entryDate;
    }

    public String getEntryId()
    {
        return entryId;
    }

    public int getEntryTime()
    {
        return entryTime;
    }

    public String getInvitationId()
    {
        return invitationId;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setAddFlag(int addFlag)
    {
        this.addFlag = addFlag;
    }

    public void setEntryId(String entryId)
    {
        this.entryId = entryId;
    }

    public void setEntryDate(int entryDate)
    {
        this.entryDate = entryDate;
    }

    public void setEntryTime(int entryTime)
    {
        this.entryTime = entryTime;
    }

    public void setInvitationId(String invitationId)
    {
        this.invitationId = invitationId;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * 友達紹介データ取得
     * 
     * @param invitaitonId 紹介ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String invitationId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_invitation WHERE invitation_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, invitationId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.invitationId = result.getString( "invitation_id" );
                    this.userId = result.getString( "user_id" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.entryId = result.getString( "entry_id" );
                    this.mailAddr = result.getString( "mail_addr" );
                    this.entryDate = result.getInt( "entry_date" );
                    this.entryTime = result.getInt( "entry_time" );
                    this.addFlag = result.getInt( "add_flag" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInvitation.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 友達紹介データ取得
     * 
     * @param invitaitonId 紹介ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataByEntryId(String entryId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_invitation WHERE entry_id = ?";
        query = query + " ORDER BY entry_date DESC, entry_time DESC";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, entryId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.invitationId = result.getString( "invitation_id" );
                    this.userId = result.getString( "user_id" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.entryId = result.getString( "entry_id" );
                    this.mailAddr = result.getString( "mail_addr" );
                    this.entryDate = result.getInt( "entry_date" );
                    this.entryTime = result.getInt( "entry_time" );
                    this.addFlag = result.getInt( "add_flag" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInvitation.getDataByEntryId] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * 友達紹介データ取得
     * 
     * @param result 友達紹介レコードセット
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.invitationId = result.getString( "invitation_id" );
                this.userId = result.getString( "user_id" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.entryId = result.getString( "entry_id" );
                this.mailAddr = result.getString( "mail_addr" );
                this.entryDate = result.getInt( "entry_date" );
                this.entryTime = result.getInt( "entry_time" );
                this.addFlag = result.getInt( "add_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInvitation.getData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * 友達紹介データ追加
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

        query = "INSERT hh_user_invitation SET ";
        query = query + " invitation_id = ?,";
        query = query + " user_id = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " entry_id = ?,";
        query = query + " mail_addr = ?,";
        query = query + " entry_date = ?,";
        query = query + " entry_time = ?,";
        query = query + " add_flag = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.invitationId );
            prestate.setString( 2, this.userId );
            prestate.setInt( 3, this.registDate );
            prestate.setInt( 4, this.registTime );
            prestate.setString( 5, this.entryId );
            prestate.setString( 6, this.mailAddr );
            prestate.setInt( 7, this.entryDate );
            prestate.setInt( 8, this.entryTime );
            prestate.setInt( 9, this.addFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInvitation.insertData] Exception=" + e.toString() );
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
     * 友達紹介データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param invitationId 紹介ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String invitationId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE  hh_user_invitation SET ";
        query = query + " user_id = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " entry_id = ?,";
        query = query + " mail_addr = ?,";
        query = query + " entry_date = ?,";
        query = query + " entry_time = ?,";
        query = query + " add_flag = ?";
        query = query + " WHERE invitation_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.userId );
            prestate.setInt( 2, this.registDate );
            prestate.setInt( 3, this.registTime );
            prestate.setString( 4, this.entryId );
            prestate.setString( 5, this.mailAddr );
            prestate.setInt( 6, this.entryDate );
            prestate.setInt( 7, this.entryTime );
            prestate.setInt( 8, this.addFlag );
            prestate.setString( 9, invitationId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserInvitation.updateData] Exception=" + e.toString() );
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
