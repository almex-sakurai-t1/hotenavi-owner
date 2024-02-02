package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Mon Feb 24 10:25:52 JST 2014
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

public class DataHotelPresentOffer implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 2397625187625521531L;
    private int               formId;
    private int               id;
    private String            title;
    private String            summary;
    private String            detail;
    private int               delFlag;
    private int               startDate;
    private int               endDate;
    private int               statusFlag;
    private int               registDate;
    private int               registTime;
    private int               lastUpdate;
    private int               lastUptime;
    private String            ownerHotelid;
    private int               ownerUserid;
    private int               electNo;
    private String            electTitle;
    private String            electBody;
    private String            loseTitle;
    private String            loseBody;
    private int               zipCodeFlag;
    private int               addressFlag;
    private int               nameFlag;
    private int               telFlag;
    private int               freewordFlag;
    private int               presentMile;
    private String            coupon;

    public DataHotelPresentOffer()
    {
        formId = 0;
        id = 0;
        title = "";
        summary = "";
        detail = "";
        delFlag = 0;
        startDate = 0;
        endDate = 0;
        registDate = 0;
        registTime = 0;
        lastUpdate = 0;
        lastUptime = 0;
        ownerHotelid = "";
        ownerUserid = 0;
        electNo = 0;
        electTitle = "";
        electBody = "";
        loseTitle = "";
        loseBody = "";
        zipCodeFlag = 0;
        addressFlag = 0;
        nameFlag = 0;
        telFlag = 0;
        freewordFlag = 0;
        presentMile = 0;
        coupon = "";
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public int getStatusFlag()
    {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag)
    {
        this.statusFlag = statusFlag;
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

    public int getElectNo()
    {
        return electNo;
    }

    public void setElectNo(int electNo)
    {
        this.electNo = electNo;
    }

    public String getElectTitle()
    {
        return electTitle;
    }

    public void setElectTitle(String electMailTitle)
    {
        this.electTitle = electMailTitle;
    }

    public String getElectBody()
    {
        return electBody;
    }

    public void setElectBody(String electMailBody)
    {
        this.electBody = electMailBody;
    }

    public String getLoseTitle()
    {
        return loseTitle;
    }

    public void setLoseTitle(String loseMailTitle)
    {
        this.loseTitle = loseMailTitle;
    }

    public String getLoseBody()
    {
        return loseBody;
    }

    public void setLoseBody(String loseMailBody)
    {
        this.loseBody = loseMailBody;
    }

    public int getZipCodeFlag()
    {
        return zipCodeFlag;
    }

    public void setZipCodeFlag(int zipCodeFlag)
    {
        this.zipCodeFlag = zipCodeFlag;
    }

    public int getAddressFlag()
    {
        return addressFlag;
    }

    public void setAddressFlag(int addressFlag)
    {
        this.addressFlag = addressFlag;
    }

    public int getNameFlag()
    {
        return nameFlag;
    }

    public void setNameFlag(int nameFlag)
    {
        this.nameFlag = nameFlag;
    }

    public int getTelFlag()
    {
        return telFlag;
    }

    public void setTelFlag(int telFlag)
    {
        this.telFlag = telFlag;
    }

    public int getFreewordFlag()
    {
        return freewordFlag;
    }

    public void setFreewordFlag(int freewordFlag)
    {
        this.freewordFlag = freewordFlag;
    }

    public int getPresentMile()
    {
        return presentMile;
    }

    public void setPresentMile(int presentMile)
    {
        this.presentMile = presentMile;
    }

    public String getCoupon()
    {
        return coupon;
    }

    public void setCoupon(String coupon)
    {
        this.coupon = coupon;
    }

    /**
     * ホテルプレゼント提供
     * 
     * @param id ホテルID
     * @param form_id 応募ID
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int form_id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_hotel_present_offer WHERE  form_id=? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, form_id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.formId = result.getInt( "form_id" );
                    this.id = result.getInt( "id" );
                    this.title = result.getString( "title" );
                    this.summary = result.getString( "summary" );
                    this.detail = result.getString( "detail" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.statusFlag = result.getInt( "status_flag" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.ownerHotelid = result.getString( "owner_hotelid" );
                    this.ownerUserid = result.getInt( "owner_userid" );
                    this.electNo = result.getInt( "elect_no" );
                    this.electTitle = result.getString( "elect_title" );
                    this.electBody = result.getString( "elect_body" );
                    this.loseTitle = result.getString( "lose_body" );
                    this.loseBody = result.getString( "lose_body" );
                    this.zipCodeFlag = result.getInt( "zip_code_flag" );
                    this.addressFlag = result.getInt( "address_flag" );
                    this.nameFlag = result.getInt( "name_flag" );
                    this.telFlag = result.getInt( "tel_flag" );
                    this.freewordFlag = result.getInt( "freeword_flag" );
                    this.presentMile = result.getInt( "present_mile" );
                    this.coupon = result.getString( "coupon" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelPresentOffer.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテルプレゼント提供取得
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
                this.formId = result.getInt( "form_id" );
                this.id = result.getInt( "id" );
                this.title = result.getString( "title" );
                this.summary = result.getString( "summary" );
                this.detail = result.getString( "detail" );
                this.delFlag = result.getInt( "del_flag" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.statusFlag = result.getInt( "status_flag" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.ownerHotelid = result.getString( "owner_hotelid" );
                this.ownerUserid = result.getInt( "owner_userid" );
                this.electNo = result.getInt( "elect_no" );
                this.electTitle = result.getString( "elect_title" );
                this.electBody = result.getString( "elect_body" );
                this.loseTitle = result.getString( "lose_body" );
                this.loseBody = result.getString( "lose_body" );
                this.zipCodeFlag = result.getInt( "zip_code_flag" );
                this.addressFlag = result.getInt( "address_flag" );
                this.nameFlag = result.getInt( "name_flag" );
                this.telFlag = result.getInt( "tel_flag" );
                this.freewordFlag = result.getInt( "freeword_flag" );
                this.presentMile = result.getInt( "present_mile" );
                this.coupon = result.getString( "coupon" );

                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelPresentOffer.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * ホテルプレゼント提供追加
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

        query = "INSERT hh_hotel_present_offer SET ";
        query += " form_id = ?,";
        query += " id = ?,";
        query += " title = ?,";
        query += " summary = ?,";
        query += " detail = ?,";
        query += " del_flag = ?,";
        query += " start_date = ?,";
        query += " end_date = ?,";
        query += " status_flag = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query += " last_update = ?,";
        query += " last_uptime = ?";
        query += " owner_hotelid = ?,";
        query += " owner_userid = ?,";
        query += " elect_no = ?,";
        query += " elect_title = ?,";
        query += " elect_body = ?,";
        query += " lose_title = ?,";
        query += " lose_body = ?,";
        query += " zip_code_flag = ?,";
        query += " address_flag = ?,";
        query += " name_flag = ?,";
        query += " tel_flag = ?,";
        query += " freeword_flag = ?,";
        query += " present_mile = ?,";
        query += " coupon = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( ++i, this.formId );
            prestate.setInt( ++i, this.id );
            prestate.setString( ++i, this.title );
            prestate.setString( ++i, this.summary );
            prestate.setString( ++i, this.detail );
            prestate.setInt( ++i, this.delFlag );
            prestate.setInt( ++i, this.startDate );
            prestate.setInt( ++i, this.endDate );
            prestate.setInt( ++i, this.statusFlag );
            prestate.setInt( ++i, this.registDate );
            prestate.setInt( ++i, this.registTime );
            prestate.setInt( ++i, this.lastUpdate );
            prestate.setInt( ++i, this.lastUptime );
            prestate.setString( ++i, this.ownerHotelid );
            prestate.setInt( ++i, this.ownerUserid );
            prestate.setInt( ++i, this.electNo );
            prestate.setString( ++i, this.electTitle );
            prestate.setString( ++i, this.electBody );
            prestate.setString( ++i, this.loseTitle );
            prestate.setString( ++i, this.loseBody );
            prestate.setInt( ++i, this.zipCodeFlag );
            prestate.setInt( ++i, this.addressFlag );
            prestate.setInt( ++i, this.nameFlag );
            prestate.setInt( ++i, this.telFlag );
            prestate.setInt( ++i, this.freewordFlag );
            prestate.setInt( ++i, this.presentMile );
            prestate.setString( ++i, this.coupon );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelPresentOffer.insertData] Exception=" + e.toString() );
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
     * ホテルプレゼント提供変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @param form_id 応募ID
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int formId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        int i = 0;
        ret = false;

        query = "UPDATE hh_hotel_present_offer SET ";
        query += " id = ?";
        query += " title = ?,";
        query += " summary = ?,";
        query += " detail = ?,";
        query += " del_flag = ?,";
        query += " start_date = ?,";
        query += " end_date = ?,";
        query += " status_flag = ?,";
        query += " regist_date = ?,";
        query += " regist_time = ?,";
        query += " last_update = ?,";
        query += " last_uptime = ?,";
        query += " owner_hotelid = ?,";
        query += " owner_userid = ?,";
        query += " elect_no = ?,";
        query += " elect_title = ?,";
        query += " elect_body = ?,";
        query += " lose_title = ?,";
        query += " lose_body = ?,";
        query += " zip_code_flag = ?,";
        query += " address_flag = ?,";
        query += " name_flag = ?,";
        query += " tel_flag = ?,";
        query += " freeword_flag = ?,";
        query += " present_mile = ?,";
        query += " coupon = ?";

        query += " WHERE form_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( ++i, this.id );
            prestate.setString( ++i, this.title );
            prestate.setString( ++i, this.summary );
            prestate.setString( ++i, this.detail );
            prestate.setInt( ++i, this.delFlag );
            prestate.setInt( ++i, this.startDate );
            prestate.setInt( ++i, this.endDate );
            prestate.setInt( ++i, this.statusFlag );
            prestate.setInt( ++i, this.registDate );
            prestate.setInt( ++i, this.registTime );
            prestate.setInt( ++i, this.lastUpdate );
            prestate.setInt( ++i, this.lastUptime );
            prestate.setString( ++i, this.ownerHotelid );
            prestate.setInt( ++i, this.ownerUserid );
            prestate.setInt( ++i, this.electNo );
            prestate.setString( ++i, this.electTitle );
            prestate.setString( ++i, this.electBody );
            prestate.setString( ++i, this.loseTitle );
            prestate.setString( ++i, this.loseBody );
            prestate.setInt( ++i, this.zipCodeFlag );
            prestate.setInt( ++i, this.addressFlag );
            prestate.setInt( ++i, this.nameFlag );
            prestate.setInt( ++i, this.telFlag );
            prestate.setInt( ++i, this.freewordFlag );
            prestate.setInt( ++i, this.presentMile );
            prestate.setString( ++i, this.coupon );

            prestate.setInt( ++i, formId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelPresentOffer.updateData] Exception=" + e.toString() );
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
