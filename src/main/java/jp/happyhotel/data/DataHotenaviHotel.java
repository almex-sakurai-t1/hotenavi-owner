/*
 * @(#)DataHotenaviHotel.java 1.00 2007/09/20 Copyright (C) ALMEX Inc. 2007 ホテナビホテル情報取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * ホテナビホテル情報取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/09/20
 * @version 1.1 2007/11/29
 */
public class DataHotenaviHotel implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = 176397596256994027L;

    private String            hotelId;
    private String            name;
    private String            password;
    private String            mail;
    private String            url;
    private String            ftpServer;
    private String            ftpPasswd;
    private String            salesDir;
    private String            accessDir;
    private String            centerId;
    private String            frontIp;
    private String            reportIp;
    private String            objectNo;
    private String            ownerUser;
    private String            ownerPassword;
    private int               plan;
    private String            memo;
    private String            reportUser;
    private String            reportPassword;
    private int               reportCenter;
    private int               hostKind;
    private String            groupId;
    private String            groupMag;
    private int               hostDetail;
    private String            tenpoId;
    private int               groupFlag;
    private int               midLength;
    private int               entryMethod;
    private int               reportTimes;
    private int               reportTimee;
    private int               closeDay;

    /**
     * データを初期化します。
     */
    public DataHotenaviHotel()
    {
        hotelId = "";
        name = "";
        password = "";
        mail = "";
        url = "";
        ftpServer = "";
        ftpPasswd = "";
        salesDir = "";
        accessDir = "";
        centerId = "";
        frontIp = "";
        reportIp = "";
        objectNo = "";
        ownerUser = "";
        ownerPassword = "";
        plan = 0;
        memo = "";
        reportUser = "";
        reportPassword = "";
        reportCenter = 0;
        hostKind = 0;
        groupId = "";
        groupMag = "";
        hostDetail = 0;
        tenpoId = "";
        groupFlag = 0;
        midLength = 0;
        entryMethod = 0;
        reportTimes = 0;
        reportTimee = 0;
        closeDay = 0;
    }

    public String getAccessDir()
    {
        return accessDir;
    }

    public String getCenterId()
    {
        return centerId;
    }

    public int getCloseDay()
    {
        return closeDay;
    }

    public int getEntryMethod()
    {
        return entryMethod;
    }

    public String getFrontIp()
    {
        return frontIp;
    }

    public String getFtpPasswd()
    {
        return ftpPasswd;
    }

    public String getFtpServer()
    {
        return ftpServer;
    }

    public int getGroupFlag()
    {
        return groupFlag;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public String getGroupMag()
    {
        return groupMag;
    }

    public int getHostDetail()
    {
        return hostDetail;
    }

    public int getHostKind()
    {
        return hostKind;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public String getMail()
    {
        return mail;
    }

    public String getMemo()
    {
        return memo;
    }

    public int getMidLength()
    {
        return midLength;
    }

    public String getName()
    {
        return name;
    }

    public String getObjectNo()
    {
        return objectNo;
    }

    public String getOwnerPassword()
    {
        return ownerPassword;
    }

    public String getOwnerUser()
    {
        return ownerUser;
    }

    public String getPassword()
    {
        return password;
    }

    public int getPlan()
    {
        return plan;
    }

    public int getReportCenter()
    {
        return reportCenter;
    }

    public String getReportIp()
    {
        return reportIp;
    }

    public String getReportPassword()
    {
        return reportPassword;
    }

    public int getReportTimee()
    {
        return reportTimee;
    }

    public int getReportTimes()
    {
        return reportTimes;
    }

    public String getReportUser()
    {
        return reportUser;
    }

    public String getSalesDir()
    {
        return salesDir;
    }

    public String getTenpoId()
    {
        return tenpoId;
    }

    public String getUrl()
    {
        return url;
    }

    public void setAccessDir(String accessDir)
    {
        this.accessDir = accessDir;
    }

    public void setCenterId(String centerId)
    {
        this.centerId = centerId;
    }

    public void setCloseDay(int closeDay)
    {
        this.closeDay = closeDay;
    }

    public void setEntryMethod(int entryMethod)
    {
        this.entryMethod = entryMethod;
    }

    public void setFrontIp(String frontIp)
    {
        this.frontIp = frontIp;
    }

    public void setFtpPasswd(String ftpPasswd)
    {
        this.ftpPasswd = ftpPasswd;
    }

    public void setFtpServer(String ftpServer)
    {
        this.ftpServer = ftpServer;
    }

    public void setGroupFlag(int groupFlag)
    {
        this.groupFlag = groupFlag;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public void setGroupMag(String groupMag)
    {
        this.groupMag = groupMag;
    }

    public void setHostDetail(int hostDetail)
    {
        this.hostDetail = hostDetail;
    }

    public void setHostKind(int hostKind)
    {
        this.hostKind = hostKind;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public void setMidLength(int midLength)
    {
        this.midLength = midLength;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setObjectNo(String objectNo)
    {
        this.objectNo = objectNo;
    }

    public void setOwnerPassword(String ownerPassword)
    {
        this.ownerPassword = ownerPassword;
    }

    public void setOwnerUser(String ownerUser)
    {
        this.ownerUser = ownerUser;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setPlan(int plan)
    {
        this.plan = plan;
    }

    public void setReportCenter(int reportCenter)
    {
        this.reportCenter = reportCenter;
    }

    public void setReportIp(String reportIp)
    {
        this.reportIp = reportIp;
    }

    public void setReportPassword(String reportPassword)
    {
        this.reportPassword = reportPassword;
    }

    public void setReportTimee(int reportTimee)
    {
        this.reportTimee = reportTimee;
    }

    public void setReportTimes(int reportTimes)
    {
        this.reportTimes = reportTimes;
    }

    public void setReportUser(String reportUser)
    {
        this.reportUser = reportUser;
    }

    public void setSalesDir(String salesDir)
    {
        this.salesDir = salesDir;
    }

    public void setTenpoId(String tenpoId)
    {
        this.tenpoId = tenpoId;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * ホテナビホテル基本データ取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String hotelId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hotel WHERE hotel_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.hotelId = result.getString( "hotel_id" );
                    this.name = result.getString( "name" );
                    this.password = result.getString( "password" );
                    this.mail = result.getString( "mail" );
                    this.url = result.getString( "url" );
                    this.ftpServer = result.getString( "ftp_server" );
                    this.ftpPasswd = result.getString( "ftp_passwd" );
                    this.salesDir = result.getString( "sales_dir" );
                    this.accessDir = result.getString( "access_dir" );
                    this.centerId = result.getString( "center_id" );
                    this.frontIp = result.getString( "front_ip" );
                    this.reportIp = result.getString( "report_ip" );
                    this.objectNo = result.getString( "object_no" );
                    this.ownerUser = result.getString( "owner_user" );
                    this.ownerPassword = result.getString( "owner_password" );
                    this.plan = result.getInt( "plan" );
                    this.memo = result.getString( "memo" );
                    this.reportUser = result.getString( "report_user" );
                    this.reportPassword = result.getString( "report_password" );
                    this.reportCenter = result.getInt( "report_center" );
                    this.hostKind = result.getInt( "host_kind" );
                    this.groupId = result.getString( "group_id" );
                    this.groupMag = result.getString( "group_mag" );
                    this.hostDetail = result.getInt( "host_detail" );
                    this.tenpoId = result.getString( "tenpoid" );
                    this.groupFlag = result.getInt( "group_flag" );
                    this.midLength = result.getInt( "midlength" );
                    this.entryMethod = result.getInt( "entry_method" );
                    this.reportTimes = result.getInt( "report_times" );
                    this.reportTimee = result.getInt( "report_timee" );
                    this.closeDay = result.getInt( "close_day" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotenaviHotel.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテナビホテルデータ設定
     * 
     * @param result ホテナビホテルデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.hotelId = result.getString( "hotel_id" );
                this.name = result.getString( "name" );
                this.password = result.getString( "password" );
                this.mail = result.getString( "mail" );
                this.url = result.getString( "url" );
                this.ftpServer = result.getString( "ftp_server" );
                this.ftpPasswd = result.getString( "ftp_passwd" );
                this.salesDir = result.getString( "sales_dir" );
                this.accessDir = result.getString( "access_dir" );
                this.centerId = result.getString( "center_id" );
                this.frontIp = result.getString( "front_ip" );
                this.reportIp = result.getString( "report_ip" );
                this.objectNo = result.getString( "object_no" );
                this.ownerUser = result.getString( "owner_user" );
                this.ownerPassword = result.getString( "owner_password" );
                this.plan = result.getInt( "plan" );
                this.memo = result.getString( "memo" );
                this.reportUser = result.getString( "report_user" );
                this.reportPassword = result.getString( "report_password" );
                this.reportCenter = result.getInt( "report_center" );
                this.hostKind = result.getInt( "host_kind" );
                this.groupId = result.getString( "group_id" );
                this.groupMag = result.getString( "group_mag" );
                this.hostDetail = result.getInt( "host_detail" );
                this.tenpoId = result.getString( "tenpoid" );
                this.groupFlag = result.getInt( "group_flag" );
                this.midLength = result.getInt( "midlength" );
                this.entryMethod = result.getInt( "entry_method" );
                this.reportTimes = result.getInt( "report_times" );
                this.reportTimee = result.getInt( "report_timee" );
                this.closeDay = result.getInt( "close_day" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotenaviHotel.setData] Exception=" + e.toString() );
        }

        return(true);
    }

}
