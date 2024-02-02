/*
 * @(#)DataHotelMaster.java 1.00 2007/07/17 Copyright (C) ALMEX Inc. 2007 ホテル設定情報データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテル設定情報データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/17
 * @version 1.1 2007/11/16
 */
public class DataHotelMaster implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -4323030228726301545L;

    private int               id;
    private int               bbsConfig;
    private int               mobileCoupon;
    private int               mailFlag1;
    private int               mailFlag2;
    private int               mailFlag3;
    private int               mailFlag4;
    private int               mailFlag5;
    private int               mailFlag6;
    private int               mailFlag7;
    private int               mailFlag8;
    private int               mailFlag9;
    private int               mailFlag10;
    private int               mailStartTime;
    private int               mailEndTime;
    private int               dispRoomMax;
    private int               emptyDispKind;
    private int               emptyDispType;
    private int               cleanDispKind;
    private int               cleanDispType;
    private String            emptyDispMessage;
    private String            fullDispMessage;
    private int               hotenaviEmptyFlag;
    private String            frontIp;
    private int               useFrontDispFlag;
    private String            globalIp;
    private String            hotenaviId;
    private int               touchSyncFlag;
    private int               pmsFlag;

    /**
     * データを初期化します。
     */
    public DataHotelMaster()
    {
        id = 0;
        bbsConfig = 0;
        mobileCoupon = 0;
        mailFlag1 = 0;
        mailFlag2 = 0;
        mailFlag3 = 0;
        mailFlag4 = 0;
        mailFlag5 = 0;
        mailFlag6 = 0;
        mailFlag7 = 0;
        mailFlag8 = 0;
        mailFlag9 = 0;
        mailFlag10 = 0;
        mailStartTime = 0;
        mailEndTime = 0;
        dispRoomMax = 0;
        emptyDispKind = 0;
        emptyDispType = 0;
        cleanDispKind = 0;
        cleanDispType = 0;
        emptyDispMessage = "";
        fullDispMessage = "";
        hotenaviEmptyFlag = 0;
        frontIp = "";
        useFrontDispFlag = 0;
        globalIp = "";
        hotenaviId = "";
        touchSyncFlag = 0;
        pmsFlag = 0;
    }

    /* getter */
    public int getBbsConfig()
    {
        return bbsConfig;
    }

    public int getCleanDispKind()
    {
        return cleanDispKind;
    }

    public int getCleanDispType()
    {
        return cleanDispType;
    }

    public int getDispRoomMax()
    {
        return dispRoomMax;
    }

    public int getEmptyDispKind()
    {
        return emptyDispKind;
    }

    public String getEmptyDispMessage()
    {
        return emptyDispMessage;
    }

    public int getEmptyDispType()
    {
        return emptyDispType;
    }

    public String getFullDispMessage()
    {
        return fullDispMessage;
    }

    public int getHotenaviEmptyFlag()
    {
        return hotenaviEmptyFlag;
    }

    public int getId()
    {
        return id;
    }

    public int getMailEndTime()
    {
        return mailEndTime;
    }

    public int getMailFlag1()
    {
        return mailFlag1;
    }

    public int getMailFlag10()
    {
        return mailFlag10;
    }

    public int getMailFlag2()
    {
        return mailFlag2;
    }

    public int getMailFlag3()
    {
        return mailFlag3;
    }

    public int getMailFlag4()
    {
        return mailFlag4;
    }

    public int getMailFlag5()
    {
        return mailFlag5;
    }

    public int getMailFlag6()
    {
        return mailFlag6;
    }

    public int getMailFlag7()
    {
        return mailFlag7;
    }

    public int getMailFlag8()
    {
        return mailFlag8;
    }

    public int getMailFlag9()
    {
        return mailFlag9;
    }

    public int getMailStartTime()
    {
        return mailStartTime;
    }

    public int getMobileCoupon()
    {
        return mobileCoupon;
    }

    public String getFrontIp()
    {
        return frontIp;
    }

    public int getUseFrontDispFlag()
    {
        return useFrontDispFlag;
    }

    public String getGlobalIp()
    {
        return globalIp;
    }

    public String getHotenaviId()
    {
        return hotenaviId;
    }

    public int getTouchSyncFlag()
    {
        return touchSyncFlag;
    }

    public int getPmsFlag()
    {
        return pmsFlag;
    }

    /* setter */
    public void setBbsConfig(int bbsConfig)
    {
        this.bbsConfig = bbsConfig;
    }

    public void setCleanDispKind(int cleanDispKind)
    {
        this.cleanDispKind = cleanDispKind;
    }

    public void setCleanDispType(int cleanDispType)
    {
        this.cleanDispType = cleanDispType;
    }

    public void setDispRoomMax(int dispRoomMax)
    {
        this.dispRoomMax = dispRoomMax;
    }

    public void setEmptyDispKind(int emptyDispKind)
    {
        this.emptyDispKind = emptyDispKind;
    }

    public void setEmptyDispMessage(String emptyDispMessage)
    {
        this.emptyDispMessage = emptyDispMessage;
    }

    public void setEmptyDispType(int emptyDispType)
    {
        this.emptyDispType = emptyDispType;
    }

    public void setFullDispMessage(String fullDispMessage)
    {
        this.fullDispMessage = fullDispMessage;
    }

    public void setHotenaviEmptyFlag(int hotenaviEmptyFlag)
    {
        this.hotenaviEmptyFlag = hotenaviEmptyFlag;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMailEndTime(int mailEndTime)
    {
        this.mailEndTime = mailEndTime;
    }

    public void setMailFlag1(int mailFlag1)
    {
        this.mailFlag1 = mailFlag1;
    }

    public void setMailFlag10(int mailFlag10)
    {
        this.mailFlag10 = mailFlag10;
    }

    public void setMailFlag2(int mailFlag2)
    {
        this.mailFlag2 = mailFlag2;
    }

    public void setMailFlag3(int mailFlag3)
    {
        this.mailFlag3 = mailFlag3;
    }

    public void setMailFlag4(int mailFlag4)
    {
        this.mailFlag4 = mailFlag4;
    }

    public void setMailFlag5(int mailFlag5)
    {
        this.mailFlag5 = mailFlag5;
    }

    public void setMailFlag6(int mailFlag6)
    {
        this.mailFlag6 = mailFlag6;
    }

    public void setMailFlag7(int mailFlag7)
    {
        this.mailFlag7 = mailFlag7;
    }

    public void setMailFlag8(int mailFlag8)
    {
        this.mailFlag8 = mailFlag8;
    }

    public void setMailFlag9(int mailFlag9)
    {
        this.mailFlag9 = mailFlag9;
    }

    public void setMailStartTime(int mailStartTime)
    {
        this.mailStartTime = mailStartTime;
    }

    public void setMobileCoupon(int mobileCoupon)
    {
        this.mobileCoupon = mobileCoupon;
    }

    public void setFrontIp(String frontIp)
    {
        this.frontIp = frontIp;
    }

    public void setUseFrontDispFlag(int useFrontDispFlag)
    {
        this.useFrontDispFlag = useFrontDispFlag;
    }

    public void setGlobalIp(String globalIp)
    {
        this.globalIp = globalIp;
    }

    public void setTouchSyncFlag(int touchSyncFlag)
    {
        this.touchSyncFlag = touchSyncFlag;
    }

    public void setPmsFlag(int pmsFlag)
    {
        this.pmsFlag = pmsFlag;
    }

    /**
     * ホテル設定情報データ取得
     * 
     * @param hotelId ホテルコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int hotelId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_hotel_master WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.id = result.getInt( "id" );
                    this.bbsConfig = result.getInt( "bbs_config" );
                    this.mobileCoupon = result.getInt( "mobile_coupon" );
                    this.mailFlag1 = result.getInt( "mail_flag1" );
                    this.mailFlag2 = result.getInt( "mail_flag2" );
                    this.mailFlag3 = result.getInt( "mail_flag3" );
                    this.mailFlag4 = result.getInt( "mail_flag4" );
                    this.mailFlag5 = result.getInt( "mail_flag5" );
                    this.mailFlag6 = result.getInt( "mail_flag6" );
                    this.mailFlag7 = result.getInt( "mail_flag7" );
                    this.mailFlag8 = result.getInt( "mail_flag8" );
                    this.mailFlag9 = result.getInt( "mail_flag9" );
                    this.mailFlag10 = result.getInt( "mail_flag10" );
                    this.mailStartTime = result.getInt( "mail_start_time" );
                    this.mailEndTime = result.getInt( "mail_end_time" );
                    this.dispRoomMax = result.getInt( "disp_room_max" );
                    this.emptyDispKind = result.getInt( "empty_disp_kind" );
                    this.emptyDispType = result.getInt( "empty_disp_type" );
                    this.cleanDispKind = result.getInt( "clean_disp_kind" );
                    this.cleanDispType = result.getInt( "clean_disp_type" );
                    this.emptyDispMessage = result.getString( "empty_disp_message" );
                    this.fullDispMessage = result.getString( "full_disp_message" );
                    this.hotenaviEmptyFlag = result.getInt( "hotenavi_empty_flag" );
                    this.frontIp = result.getString( "front_ip" );
                    this.useFrontDispFlag = result.getInt( "use_front_disp_flag" );
                    this.globalIp = result.getString( "global_ip" );
                    this.touchSyncFlag = result.getInt( "touch_sync_flag" );
                    this.pmsFlag = result.getInt( "pms_flag" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMaster.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ホテナビID取得
     * 
     * @param globalIp ホテルのグローバルIP
     * @return
     * @return 処理結果(true:異常,false:正常)
     */
    public boolean getHotelId(String globalIp)
    {
        String query;
        boolean ret = false;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_master.id,hh_hotel_basic.hotenavi_id FROM hh_hotel_master";
        query += " INNER JOIN hh_hotel_basic ON hh_hotel_basic.id = hh_hotel_master.id ";
        query += " WHERE hh_hotel_master.global_ip = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, globalIp );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.hotenaviId = result.getString( "hh_hotel_basic.hotenavi_id" );
                    this.id = result.getInt( "hh_hotel_master.id" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelIp.getHotenaviIp] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテル設定情報データ設定
     * 
     * @param result ホテル設定情報データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.id = result.getInt( "id" );
                this.bbsConfig = result.getInt( "bbs_config" );
                this.mobileCoupon = result.getInt( "mobile_coupon" );
                this.mailFlag1 = result.getInt( "mail_flag1" );
                this.mailFlag2 = result.getInt( "mail_flag2" );
                this.mailFlag3 = result.getInt( "mail_flag3" );
                this.mailFlag4 = result.getInt( "mail_flag4" );
                this.mailFlag5 = result.getInt( "mail_flag5" );
                this.mailFlag6 = result.getInt( "mail_flag6" );
                this.mailFlag7 = result.getInt( "mail_flag7" );
                this.mailFlag8 = result.getInt( "mail_flag8" );
                this.mailFlag9 = result.getInt( "mail_flag9" );
                this.mailFlag10 = result.getInt( "mail_flag10" );
                this.mailStartTime = result.getInt( "mail_start_time" );
                this.mailEndTime = result.getInt( "mail_end_time" );
                this.dispRoomMax = result.getInt( "disp_room_max" );
                this.emptyDispKind = result.getInt( "empty_disp_kind" );
                this.emptyDispType = result.getInt( "empty_disp_type" );
                this.cleanDispKind = result.getInt( "clean_disp_kind" );
                this.cleanDispType = result.getInt( "clean_disp_type" );
                this.emptyDispMessage = result.getString( "empty_disp_message" );
                this.fullDispMessage = result.getString( "full_disp_message" );
                this.hotenaviEmptyFlag = result.getInt( "hotenavi_empty_flag" );
                this.frontIp = result.getString( "front_ip" );
                this.useFrontDispFlag = result.getInt( "use_front_disp_flag" );
                this.globalIp = result.getString( "global_ip" );
                this.touchSyncFlag = result.getInt( "touch_sync_flag" );
                this.pmsFlag = result.getInt( "pms_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMaster.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ホテル設定情報データ追加
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

        query = "INSERT hh_hotel_master SET ";
        query = query + " id = ?,";
        query = query + " bbs_config = ?,";
        query = query + " mobile_coupon = ?,";
        query = query + " mail_flag1 = ?,";
        query = query + " mail_flag2 = ?,";
        query = query + " mail_flag3 = ?,";
        query = query + " mail_flag4 = ?,";
        query = query + " mail_flag5 = ?,";
        query = query + " mail_flag6 = ?,";
        query = query + " mail_flag7 = ?,";
        query = query + " mail_flag8 = ?,";
        query = query + " mail_flag9 = ?,";
        query = query + " mail_flag10 = ?,";
        query = query + " mail_start_time = ?,";
        query = query + " mail_end_time = ?,";
        query = query + " disp_room_max = ?,";
        query = query + " empty_disp_kind = ?,";
        query = query + " empty_disp_type = ?,";
        query = query + " clean_disp_kind = ?,";
        query = query + " clean_disp_type = ?,";
        query = query + " empty_disp_message = ?,";
        query = query + " full_disp_message = ?,";
        query = query + " hotenavi_empty_flag = ?,";
        query = query + " front_ip = ?,";
        query = query + " use_front_disp_flag = ?,";
        query = query + " global_ip = ?,";
        query = query + " touch_sync_flag = ?,";
        query = query + " pms_flag = ?";

        try
        {

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.id );
            prestate.setInt( 2, this.bbsConfig );
            prestate.setInt( 3, this.mobileCoupon );
            prestate.setInt( 4, this.mailFlag1 );
            prestate.setInt( 5, this.mailFlag2 );
            prestate.setInt( 6, this.mailFlag3 );
            prestate.setInt( 7, this.mailFlag4 );
            prestate.setInt( 8, this.mailFlag5 );
            prestate.setInt( 9, this.mailFlag6 );
            prestate.setInt( 10, this.mailFlag7 );
            prestate.setInt( 11, this.mailFlag8 );
            prestate.setInt( 12, this.mailFlag9 );
            prestate.setInt( 13, this.mailFlag10 );
            prestate.setInt( 14, this.mailStartTime );
            prestate.setInt( 15, this.mailEndTime );
            prestate.setInt( 16, this.dispRoomMax );
            prestate.setInt( 17, this.emptyDispKind );
            prestate.setInt( 18, this.emptyDispType );
            prestate.setInt( 19, this.cleanDispKind );
            prestate.setInt( 20, this.cleanDispType );
            prestate.setString( 21, this.emptyDispMessage );
            prestate.setString( 22, this.fullDispMessage );
            prestate.setInt( 23, this.hotenaviEmptyFlag );
            prestate.setString( 24, this.frontIp );
            prestate.setInt( 25, this.useFrontDispFlag );
            prestate.setString( 26, this.globalIp );
            prestate.setInt( 27, this.touchSyncFlag );
            prestate.setInt( 28, this.pmsFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMaster.insertData] Exception=" + e.toString() );
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
     * ホテル設定情報データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int id)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_hotel_master SET ";
        query = query + " bbs_config = ?,";
        query = query + " mobile_coupon = ?,";
        query = query + " mail_flag1 = ?,";
        query = query + " mail_flag2 = ?,";
        query = query + " mail_flag3 = ?,";
        query = query + " mail_flag4 = ?,";
        query = query + " mail_flag5 = ?,";
        query = query + " mail_flag6 = ?,";
        query = query + " mail_flag7 = ?,";
        query = query + " mail_flag8 = ?,";
        query = query + " mail_flag9 = ?,";
        query = query + " mail_flag10 = ?,";
        query = query + " mail_start_time = ?,";
        query = query + " mail_end_time = ?,";
        query = query + " disp_room_max = ?,";
        query = query + " empty_disp_kind = ?,";
        query = query + " empty_disp_type = ?,";
        query = query + " clean_disp_kind = ?,";
        query = query + " clean_disp_type = ?,";
        query = query + " empty_disp_message = ?,";
        query = query + " full_disp_message = ?,";
        query = query + " hotenavi_empty_flag = ?,";
        query = query + " front_ip = ?,";
        query = query + " use_front_disp_flag = ?,";
        query = query + " global_ip = ?,";
        query = query + " touch_sync_flag = ?,";
        query = query + " pms_flag = ?";
        query = query + " WHERE id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( 1, this.bbsConfig );
            prestate.setInt( 2, this.mobileCoupon );
            prestate.setInt( 3, this.mailFlag1 );
            prestate.setInt( 4, this.mailFlag2 );
            prestate.setInt( 5, this.mailFlag3 );
            prestate.setInt( 6, this.mailFlag4 );
            prestate.setInt( 7, this.mailFlag5 );
            prestate.setInt( 8, this.mailFlag6 );
            prestate.setInt( 9, this.mailFlag7 );
            prestate.setInt( 10, this.mailFlag8 );
            prestate.setInt( 11, this.mailFlag9 );
            prestate.setInt( 12, this.mailFlag10 );
            prestate.setInt( 13, this.mailStartTime );
            prestate.setInt( 14, this.mailEndTime );
            prestate.setInt( 15, this.dispRoomMax );
            prestate.setInt( 16, this.emptyDispKind );
            prestate.setInt( 17, this.emptyDispType );
            prestate.setInt( 18, this.cleanDispKind );
            prestate.setInt( 19, this.cleanDispType );
            prestate.setString( 20, this.emptyDispMessage );
            prestate.setString( 21, this.fullDispMessage );
            prestate.setInt( 22, this.hotenaviEmptyFlag );
            prestate.setString( 23, this.frontIp );
            prestate.setInt( 24, this.useFrontDispFlag );
            prestate.setString( 25, this.globalIp );
            prestate.setInt( 26, this.touchSyncFlag );
            prestate.setInt( 27, this.pmsFlag );
            prestate.setInt( 28, id );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelMaster.updateData] Exception=" + e.toString() );
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
