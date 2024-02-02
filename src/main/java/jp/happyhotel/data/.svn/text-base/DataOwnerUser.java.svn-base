/*
 * @(#)DataOwnerUser.java 1.00 2007/09/20 Copyright (C) ALMEX Inc. 2007 オーナー基本情報取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * オーナー基本情報取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/09/20
 * @version 1.1 2007/11/29
 */
public class DataOwnerUser implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -5130804732203760330L;

    private String            hotelId;
    private int               userId;
    private String            loginId;
    private String            machineId;
    private String            name;
    private String            passwdPc;
    private String            passwdMobile;
    private String            mailAddrPc;
    private String            mailAddrMobile;
    private int               level;
    private int               imediaUser;
    private int               reportFlag;
    private int               mailStartTime;
    private int               mailEndTime;

    /**
     * データを初期化します。
     */
    public DataOwnerUser()
    {
        hotelId = "";
        userId = 0;
        loginId = "";
        machineId = "";
        name = "";
        passwdPc = "";
        passwdMobile = "";
        mailAddrPc = "";
        mailAddrMobile = "";
        level = 0;
        imediaUser = 0;
        reportFlag = 0;
        mailStartTime = 0;
        mailEndTime = 0;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public int getImediaUser()
    {
        return imediaUser;
    }

    public int getLevel()
    {
        return level;
    }

    public String getLoginId()
    {
        return loginId;
    }

    public String getMachineId()
    {
        return machineId;
    }

    public String getMailAddrMobile()
    {
        return mailAddrMobile;
    }

    public String getMailAddrPc()
    {
        return mailAddrPc;
    }

    public int getMailEndTime()
    {
        return mailEndTime;
    }

    public int getMailStartTime()
    {
        return mailStartTime;
    }

    public String getName()
    {
        return name;
    }

    public String getPasswdMobile()
    {
        return passwdMobile;
    }

    public String getPasswdPc()
    {
        return passwdPc;
    }

    public int getReportFlag()
    {
        return reportFlag;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setImediaUser(int imediaUser)
    {
        this.imediaUser = imediaUser;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void setLoginId(String loginId)
    {
        this.loginId = loginId;
    }

    public void setMachineId(String machineId)
    {
        this.machineId = machineId;
    }

    public void setMailAddrMobile(String mailAddrMobile)
    {
        this.mailAddrMobile = mailAddrMobile;
    }

    public void setMailAddrPc(String mailAddrPc)
    {
        this.mailAddrPc = mailAddrPc;
    }

    public void setMailEndTime(int mailEndTime)
    {
        this.mailEndTime = mailEndTime;
    }

    public void setMailStartTime(int mailStartTime)
    {
        this.mailStartTime = mailStartTime;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPasswdMobile(String passwdMobile)
    {
        this.passwdMobile = passwdMobile;
    }

    public void setPasswdPc(String passwdPc)
    {
        this.passwdPc = passwdPc;
    }

    public void setReportFlag(int reportFlag)
    {
        this.reportFlag = reportFlag;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    /**
     * オーナー基本データ取得
     * 
     * @param hotelId ホテルID
     * @param userId ユーザID
     * @param loginId ログインID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String hotelId, int userId, String loginId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM owner_user WHERE hotelid = ? AND userid = ? AND loginid = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, hotelId );
            prestate.setInt( 2, userId );
            prestate.setString( 3, loginId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.hotelId = result.getString( "hotelid" );
                    this.userId = result.getInt( "userid" );
                    this.loginId = result.getString( "loginid" );
                    this.machineId = result.getString( "machineid" );
                    this.name = result.getString( "name" );
                    this.passwdPc = result.getString( "passwd_pc" );
                    this.passwdMobile = result.getString( "passwd_mobile" );
                    this.mailAddrPc = result.getString( "mailaddr_pc" );
                    this.mailAddrMobile = result.getString( "mailaddr_mobile" );
                    this.level = result.getInt( "level" );
                    this.imediaUser = result.getInt( "imedia_user" );
                    this.reportFlag = result.getInt( "report_flag" );
                    this.mailStartTime = result.getInt( "mail_starttime" );
                    this.mailEndTime = result.getInt( "mail_endtime" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOwnerUser.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ユーザ基本データ設定
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.hotelId = result.getString( "hotelid" );
                this.userId = result.getInt( "userid" );
                this.loginId = result.getString( "loginid" );
                this.machineId = result.getString( "machineid" );
                this.name = result.getString( "name" );
                this.passwdPc = result.getString( "passwd_pc" );
                this.passwdMobile = result.getString( "passwd_mobile" );
                this.mailAddrPc = result.getString( "mailaddr_pc" );
                this.mailAddrMobile = result.getString( "mailaddr_mobile" );
                this.level = result.getInt( "level" );
                this.imediaUser = result.getInt( "imedia_user" );
                this.reportFlag = result.getInt( "report_flag" );
                this.mailStartTime = result.getInt( "mail_starttime" );
                this.mailEndTime = result.getInt( "mail_endtime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOwnerUser.setData] Exception=" + e.toString() );
        }

        return(true);
    }

}
