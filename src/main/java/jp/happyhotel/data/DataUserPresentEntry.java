package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Mon Feb 24 10:26:12 JST 2014
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.RandomNumber;

/**
 * DataUserPresentEntry
 * 
 * 
 */
public class DataUserPresentEntry implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -7863646770708007576L;
    private String            userId;
    private int               formId;
    private int               id;
    private int               statusFlag;
    private String            mailAddr;
    private int               serialNo;
    private int               registDate;
    private int               registTime;
    private int               lastUpdate;
    private int               lastUptime;
    private String            ownerHotelid;
    private int               ownerUserid;
    private int               sendDate;
    private int               sendTime;
    private int               sendStatus;
    private String            zipCode;
    private int               prefCode;
    private String            address1;
    private String            address2;
    private String            address3;
    private String            name;
    private String            tel;
    private String            freeword;

    public DataUserPresentEntry()
    {
        userId = "";
        formId = 0;
        id = 0;
        statusFlag = 0;
        mailAddr = "";
        serialNo = 0;
        registDate = 0;
        registTime = 0;
        lastUpdate = 0;
        lastUptime = 0;
        ownerHotelid = "";
        ownerUserid = 0;
        sendDate = 0;
        sendTime = 0;
        sendStatus = 0;
        zipCode = "";
        prefCode = 0;
        address1 = "";
        address2 = "";
        address3 = "";
        name = "";
        tel = "";
        freeword = "";
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getFormId()
    {
        return formId;
    }

    public void setFormId(int formId)
    {
        this.formId = formId;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getStatusFlag()
    {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag)
    {
        this.statusFlag = statusFlag;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public int getSerialNo()
    {
        return serialNo;
    }

    public void setSerialNo(int serialNo)
    {
        this.serialNo = serialNo;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public String getOwnerHotelid()
    {
        return ownerHotelid;
    }

    public void setOwnerHotelid(String ownerHotelid)
    {
        this.ownerHotelid = ownerHotelid;
    }

    public int getOwnerUserid()
    {
        return ownerUserid;
    }

    public void setOwnerUserid(int ownerUserid)
    {
        this.ownerUserid = ownerUserid;
    }

    public int getSendDate()
    {
        return sendDate;
    }

    public void setSendDate(int sendDate)
    {
        this.sendDate = sendDate;
    }

    public int getSendTime()
    {
        return sendTime;
    }

    public void setSendTime(int sendTime)
    {
        this.sendTime = sendTime;
    }

    public int getSendStatus()
    {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus)
    {
        this.sendStatus = sendStatus;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public int getPrefCode()
    {
        return prefCode;
    }

    public void setPrefCode(int prefCode)
    {
        this.prefCode = prefCode;
    }

    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getAddress3()
    {
        return address3;
    }

    public void setAddress3(String address3)
    {
        this.address3 = address3;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getFreeword()
    {
        return freeword;
    }

    public void setFreeword(String freeword)
    {
        this.freeword = freeword;
    }

    /**
     * ユーザプレゼントエントリー
     * 
     * @param id ホテルID
     * @param formId 応募ID
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int formId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_present_entry WHERE user_id = ? AND form_id=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, formId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.formId = result.getInt( "form_id" );
                    this.id = result.getInt( "id" );
                    this.statusFlag = result.getInt( "id" );
                    this.mailAddr = result.getString( "mail_addr" );
                    this.serialNo = result.getInt( "serial_no" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.ownerHotelid = result.getString( "owner_hotelid" );
                    this.ownerUserid = result.getInt( "owner_userid" );
                    this.sendDate = result.getInt( "send_date" );
                    this.sendTime = result.getInt( "send_time" );
                    this.sendStatus = result.getInt( "send_status" );
                    this.zipCode = result.getString( "zip_code" );
                    this.prefCode = result.getInt( "pref_code" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.address3 = result.getString( "address3" );
                    this.name = result.getString( "name" );
                    this.tel = result.getString( "tel" );
                    this.freeword = result.getString( "freeword" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPresentEntry.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザプレゼントエントリー取得
     * 
     * @param result ユーザ基本データレコード
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
                this.userId = result.getString( "user_id" );
                this.formId = result.getInt( "form_id" );
                this.id = result.getInt( "id" );
                this.statusFlag = result.getInt( "id" );
                this.mailAddr = result.getString( "mail_addr" );
                this.serialNo = result.getInt( "serial_no" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.ownerHotelid = result.getString( "owner_hotelid" );
                this.ownerUserid = result.getInt( "owner_userid" );
                this.sendDate = result.getInt( "send_date" );
                this.sendTime = result.getInt( "send_time" );
                this.sendStatus = result.getInt( "send_status" );
                this.zipCode = result.getString( "zip_code" );
                this.prefCode = result.getInt( "pref_code" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.address3 = result.getString( "address3" );
                this.name = result.getString( "name" );
                this.tel = result.getString( "tel" );
                this.freeword = result.getString( "freeword" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPresentEntry.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * ユーザプレゼントエントリー追加
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
        int i = 0;

        ret = false;

        query = "INSERT hh_user_present_entry SET ";
        query += " user_id = ?,";
        query += " form_id = ?,";
        query += " id = ?,";
        query += " status_flag = ?,";
        query += " mail_addr = ?,";
        query += " serial_no = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query += " last_update = ?,";
        query += " last_uptime = ?,";
        query += " owner_hotelid = ?,";
        query += " owner_userid = ?,";
        query += " send_date = ?,";
        query += " send_time = ?,";
        query += " send_status = ?,";
        query += " zip_code =?, ";
        query += " pref_code =?, ";
        query += " address1 =?, ";
        query += " address2 =?, ";
        query += " address3 =?, ";
        query += " name =?, ";
        query += " tel =?, ";
        query += " freeword =? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( ++i, this.userId );
            prestate.setInt( ++i, this.formId );
            prestate.setInt( ++i, this.id );
            prestate.setInt( ++i, this.statusFlag );
            prestate.setString( ++i, this.mailAddr );
            prestate.setInt( ++i, this.serialNo );
            prestate.setInt( ++i, this.registDate );
            prestate.setInt( ++i, this.registTime );
            prestate.setInt( ++i, this.lastUpdate );
            prestate.setInt( ++i, this.lastUptime );
            prestate.setString( ++i, this.ownerHotelid );
            prestate.setInt( ++i, this.ownerUserid );
            prestate.setInt( ++i, this.sendDate );
            prestate.setInt( ++i, this.sendTime );
            prestate.setInt( ++i, this.sendStatus );
            prestate.setString( ++i, this.zipCode );
            prestate.setInt( ++i, this.prefCode );
            prestate.setString( ++i, this.address1 );
            prestate.setString( ++i, this.address2 );
            prestate.setString( ++i, this.address3 );
            prestate.setString( ++i, this.name );
            prestate.setString( ++i, this.tel );
            prestate.setString( ++i, this.freeword );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPresentEntry.insertData] Exception=" + e.toString() );
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
     * ユーザプレゼントエントリー追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection connection)
    {
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        int i = 0;

        ret = false;

        query = "INSERT hh_user_present_entry SET ";
        query += " user_id = ?,";
        query += " form_id = ?,";
        query += " id = ?,";
        query += " status_flag = ?,";
        query += " mail_addr = ?,";
        query += " serial_no = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query += " last_update = ?,";
        query += " last_uptime = ?,";
        query += " owner_hotelid = ?,";
        query += " owner_userid = ?,";
        query += " send_date = ?,";
        query += " send_time = ?,";
        query += " send_status = ?,";
        query += " zip_code =?, ";
        query += " pref_code =?, ";
        query += " address1 =?, ";
        query += " address2 =?, ";
        query += " address3 =?, ";
        query += " name =?, ";
        query += " tel =?, ";
        query += " freeword =? ";

        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( ++i, this.userId );
            prestate.setInt( ++i, this.formId );
            prestate.setInt( ++i, this.id );
            prestate.setInt( ++i, this.statusFlag );
            prestate.setString( ++i, this.mailAddr );
            prestate.setInt( ++i, this.serialNo );
            prestate.setInt( ++i, this.registDate );
            prestate.setInt( ++i, this.registTime );
            prestate.setInt( ++i, this.lastUpdate );
            prestate.setInt( ++i, this.lastUptime );
            prestate.setString( ++i, this.ownerHotelid );
            prestate.setInt( ++i, this.ownerUserid );
            prestate.setInt( ++i, this.sendDate );
            prestate.setInt( ++i, this.sendTime );
            prestate.setInt( ++i, this.sendStatus );
            prestate.setString( ++i, this.zipCode );
            prestate.setInt( ++i, this.prefCode );
            prestate.setString( ++i, this.address1 );
            prestate.setString( ++i, this.address2 );
            prestate.setString( ++i, this.address3 );
            prestate.setString( ++i, this.name );
            prestate.setString( ++i, this.tel );
            prestate.setString( ++i, this.freeword );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPresentEntry.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }

        return(ret);
    }

    /**
     * ユーザプレゼントエントリー変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param form_id 応募ID
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String userId, int formId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        int i = 0;
        ret = false;

        query = "UPDATE hh_user_present_entry SET ";
        query += " id = ?,";
        query += " status_flag = ?,";
        query += " mail_addr = ?,";
        query += " serial_no = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query += " last_update = ?,";
        query += " last_uptime = ?,";
        query += " owner_hotelid = ?,";
        query += " owner_userid = ?,";
        query += " send_date = ?,";
        query += " send_time = ?,";
        query += " send_status = ?,";
        query += " zip_code =?, ";
        query += " pref_code =?, ";
        query += " address1 =?, ";
        query += " address2 =?, ";
        query += " address3 =?, ";
        query += " name =?, ";
        query += " tel =?, ";
        query += " freeword =? ";

        query += " WHERE user_id = ?";
        query += " AND form_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( ++i, this.id );
            prestate.setInt( ++i, this.statusFlag );
            prestate.setString( ++i, this.mailAddr );
            prestate.setInt( ++i, this.serialNo );
            prestate.setInt( ++i, this.registDate );
            prestate.setInt( ++i, this.registTime );
            prestate.setInt( ++i, this.lastUpdate );
            prestate.setInt( ++i, this.lastUptime );
            prestate.setString( ++i, this.ownerHotelid );
            prestate.setInt( ++i, this.ownerUserid );
            prestate.setInt( ++i, this.sendDate );
            prestate.setInt( ++i, this.sendTime );
            prestate.setInt( ++i, this.sendStatus );
            prestate.setString( ++i, this.zipCode );
            prestate.setInt( ++i, this.prefCode );
            prestate.setString( ++i, this.address1 );
            prestate.setString( ++i, this.address2 );
            prestate.setString( ++i, this.address3 );
            prestate.setString( ++i, this.name );
            prestate.setString( ++i, this.tel );
            prestate.setString( ++i, this.freeword );

            prestate.setString( ++i, userId );
            prestate.setInt( ++i, formId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserPresentEntry.updateData] Exception=" + e.toString() );
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
     * シリアルNo取得
     * 
     * @param userId ユーザーID
     * @param formId 応募ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int getSerialNo(String userId, int formId)
    {

        int serialNo = 0;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
        int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );

        DataUserPresentEntry dupe = new DataUserPresentEntry();
        if ( dupe.getData( userId, formId ) )
        {
            serialNo = dupe.getSerialNo();
        }
        else
        {
            query = "SELECT serial_no FROM hh_user_present_entry WHERE form_id=? ORDER BY serial_no DESC";
            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                prestate.setInt( 1, formId );
                result = prestate.executeQuery();
                if ( result != null )
                {
                    if ( result.next() != false )
                    {
                        serialNo = result.getInt( "serial_no" );
                    }
                }
                serialNo = (serialNo / 100 + 1) * 100 + RandomNumber.getRandomNumber( 2 );
                DBConnection.releaseResources( result );
                DBConnection.releaseResources( prestate );
                setUserId( userId );
                setFormId( formId );
                setSerialNo( serialNo );
                setRegistDate( nowDate );
                setRegistTime( nowTime );
                setLastUpdate( nowDate );
                setLastUptime( nowTime );
                insertData( connection );
            }
            catch ( Exception e )
            {
                Logging.error( "[DataUserPresentEntry.getSerialNo] Exception=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( result, prestate, connection );
            }
        }
        return(serialNo);
    }
}
