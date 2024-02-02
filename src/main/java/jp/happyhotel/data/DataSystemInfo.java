/*
 * @(#)DataSystemInfo.java 1.00 2008/04/09 Copyright (C) ALMEX Inc. 2008 更新情報履歴データ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;

/**
 * 更新情報履歴データ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2008/04/09
 */
public class DataSystemInfo implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 4204613439388599203L;

    private int               dataType;
    private int               id;
    private int               dispIdx;
    private int               dispFlag;
    private int               startDate;
    private int               endDate;
    private String            title;
    private String            linkUrlIn;
    private String            linkUrlOut;
    private int               urlStartDate;
    private int               urlEndDate;
    private int               memberOnly;
    private String            msg1Title;
    private String            msg1;
    private String            msg2Title;
    private String            msg2;
    private String            msg3Title;
    private String            msg3;
    private String            msg4Title;
    private String            msg4;
    private String            msg5Title;
    private String            msg5;
    private String            msg6Title;
    private String            msg6;
    private String            msg7Title;
    private String            msg7;
    private String            msg8Title;
    private String            msg8;
    private int               addDate;
    private int               addTime;
    private String            addHotelId;
    private int               addUserId;
    private int               lastUpdate;
    private int               lastUptime;
    private String            updHotelId;
    private int               updUserId;
    private String            urlDocomo;
    private String            urlAu;
    private String            urlSoftbank;
    private String            urlSmart;
    private String            titleSmart;

    /**
     * データを初期化します。
     */
    public DataSystemInfo()
    {
        dataType = 0;
        id = 0;
        dispIdx = 0;
        dispFlag = 0;
        startDate = 0;
        endDate = 0;
        title = "";
        linkUrlIn = "";
        linkUrlOut = "";
        urlStartDate = 0;
        urlEndDate = 0;
        memberOnly = 0;
        msg1Title = "";
        msg1 = "";
        msg2Title = "";
        msg2 = "";
        msg3Title = "";
        msg3 = "";
        msg4Title = "";
        msg4 = "";
        msg5Title = "";
        msg5 = "";
        msg6Title = "";
        msg6 = "";
        msg7Title = "";
        msg7 = "";
        msg8Title = "";
        msg8 = "";
        addDate = 0;
        addTime = 0;
        addHotelId = "";
        addUserId = 0;
        lastUpdate = 0;
        lastUptime = 0;
        updHotelId = "";
        updUserId = 0;
        urlDocomo = "";
        urlAu = "";
        urlSoftbank = "";
        urlSmart = "";
        titleSmart = "";
    }

    public int getDataType()
    {
        return dataType;
    }

    public int getId()
    {
        return id;
    }

    public int getDispIdx()
    {
        return dispIdx;
    }

    public int getDispFlag()
    {
        return dispFlag;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public String getTitle()
    {
        return title;
    }

    public String getLinkUrlIn()
    {
        return Url.convertUrl( linkUrlIn );
    }

    public String getLinkUrlOut()
    {
        return Url.convertUrl( linkUrlOut );
    }

    public int getUrlStartDate()
    {
        return urlStartDate;
    }

    public int getUrlEndDate()
    {
        return urlEndDate;
    }

    public int getMemberOnly()
    {
        return memberOnly;
    }

    public String getMsg1Title()
    {
        return msg1Title;
    }

    public String getMsg1()
    {
        return msg1;
    }

    public String getMsg2Title()
    {
        return msg2Title;
    }

    public String getMsg2()
    {
        return msg2;
    }

    public String getMsg3Title()
    {
        return msg3Title;
    }

    public String getMsg3()
    {
        return msg3;
    }

    public String getMsg4Title()
    {
        return msg4Title;
    }

    public String getMsg4()
    {
        return msg4;
    }

    public String getMsg5Title()
    {
        return msg5Title;
    }

    public String getMsg5()
    {
        return msg5;
    }

    public String getMsg6Title()
    {
        return msg6Title;
    }

    public String getMsg6()
    {
        return msg6;
    }

    public String getMsg7Title()
    {
        return msg7Title;
    }

    public String getMsg7()
    {
        return msg7;
    }

    public String getMsg8Title()
    {
        return msg8Title;
    }

    public String getMsg8()
    {
        return msg8;
    }

    public int getAddDate()
    {
        return addDate;
    }

    public int getAddTime()
    {
        return addTime;
    }

    public String getAddHotelId()
    {
        return addHotelId;
    }

    public int getAddUserId()
    {
        return addUserId;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public String getUpdHotelId()
    {
        return updHotelId;
    }

    public int getUpdUserId()
    {
        return updUserId;
    }

    public String getUrlDocomo()
    {
        return Url.convertUrl( urlDocomo );
    }

    public String getUrlAu()
    {
        return Url.convertUrl( urlAu );
    }

    public String getUrlSoftbank()
    {
        return Url.convertUrl( urlSoftbank );
    }

    public String getUrlSmart()
    {
        return Url.convertUrl( urlSmart );
    }

    public String getTitleSmart()
    {
        return titleSmart;
    }

    public void setDataType(int dataType)
    {
        this.dataType = dataType;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setDispIdx(int dispIdx)
    {
        this.dispIdx = dispIdx;
    }

    public void setDispFlag(int dispFlag)
    {
        this.dispFlag = dispFlag;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setLinkUrlIn(String linkUrlIn)
    {
        this.linkUrlIn = linkUrlIn;
    }

    public void setLinkUrlOut(String linkUrlOut)
    {
        this.linkUrlOut = linkUrlOut;
    }

    public void setUrlStartDate(int urlStartDate)
    {
        this.urlStartDate = urlStartDate;
    }

    public void setUrlEndDate(int urlEndDate)
    {
        this.urlEndDate = urlEndDate;
    }

    public void setMemberOnly(int memberOnly)
    {
        this.memberOnly = memberOnly;
    }

    public void setMsg1Title(String msg1Title)
    {
        this.msg1Title = msg1Title;
    }

    public void setMsg1(String msg1)
    {
        this.msg1 = msg1;
    }

    public void setMsg2Title(String msg2Title)
    {
        this.msg2Title = msg2Title;
    }

    public void setMsg2(String msg2)
    {
        this.msg2 = msg2;
    }

    public void setMsg3Title(String msg3Title)
    {
        this.msg3Title = msg3Title;
    }

    public void setMsg3(String msg3)
    {
        this.msg3 = msg3;
    }

    public void setMsg4Title(String msg4Title)
    {
        this.msg4Title = msg4Title;
    }

    public void setMsg4(String msg4)
    {
        this.msg4 = msg4;
    }

    public void setMsg5Title(String msg5Title)
    {
        this.msg5Title = msg5Title;
    }

    public void setMsg5(String msg5)
    {
        this.msg5 = msg5;
    }

    public void setMsg6Title(String msg6Title)
    {
        this.msg6Title = msg6Title;
    }

    public void setMsg6(String msg6)
    {
        this.msg6 = msg6;
    }

    public void setMsg7Title(String msg7Title)
    {
        this.msg7Title = msg7Title;
    }

    public void setMsg7(String msg7)
    {
        this.msg7 = msg7;
    }

    public void setMsg8Title(String msg8Title)
    {
        this.msg8Title = msg8Title;
    }

    public void setMsg8(String msg8)
    {
        this.msg8 = msg8;
    }

    public void setAddDate(int addDate)
    {
        this.addDate = addDate;
    }

    public void setAddTime(int addTime)
    {
        this.addTime = addTime;
    }

    public void setAddHotelId(String addHotelId)
    {
        this.addHotelId = addHotelId;
    }

    public void setAddUserId(int addUserId)
    {
        this.addUserId = addUserId;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setUpdHotelId(String updHotelId)
    {
        this.updHotelId = updHotelId;
    }

    public void setUpdUserId(int updUserId)
    {
        this.updUserId = updUserId;
    }

    public void setUrlDocomo(String urlDocomo)
    {
        this.urlDocomo = urlDocomo;
    }

    public void setUrlAu(String urlAu)
    {
        this.urlAu = urlAu;
    }

    public void setUrlSoftbank(String urlSoftbank)
    {
        this.urlSoftbank = urlSoftbank;
    }

    public void setUrlSmart(String urlSmart)
    {
        this.urlSmart = urlSmart;
    }

    public void setTitleSmart(String titleSmart)
    {
        this.titleSmart = titleSmart;
    }

    /**
     * 更新情報履歴データ取得
     * 
     * @param dataType 表示タイプ
     * @param id 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int dataType, int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_system_info WHERE data_type = ? AND id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, dataType );
            prestate.setInt( 2, id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.dataType = result.getInt( "data_type" );
                    this.id = result.getInt( "id" );
                    this.dispIdx = result.getInt( "disp_idx" );
                    this.dispFlag = result.getInt( "disp_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.title = result.getString( "title" );
                    this.linkUrlIn = result.getString( "link_url_in" );
                    this.linkUrlOut = result.getString( "link_url_out" );
                    this.urlStartDate = result.getInt( "url_start_date" );
                    this.urlEndDate = result.getInt( "url_end_date" );
                    this.memberOnly = result.getInt( "member_only" );
                    this.msg1Title = result.getString( "msg1_title" );
                    this.msg1 = result.getString( "msg1" );
                    this.msg2Title = result.getString( "msg2_title" );
                    this.msg2 = result.getString( "msg2" );
                    this.msg3Title = result.getString( "msg3_title" );
                    this.msg3 = result.getString( "msg3" );
                    this.msg4Title = result.getString( "msg4_title" );
                    this.msg4 = result.getString( "msg4" );
                    this.msg5Title = result.getString( "msg5_title" );
                    this.msg5 = result.getString( "msg5" );
                    this.msg6Title = result.getString( "msg6_title" );
                    this.msg6 = result.getString( "msg6" );
                    this.msg7Title = result.getString( "msg7_title" );
                    this.msg7 = result.getString( "msg7" );
                    this.msg8Title = result.getString( "msg8_title" );
                    this.msg8 = result.getString( "msg8" );
                    this.addDate = result.getInt( "add_date" );
                    this.addTime = result.getInt( "add_time" );
                    this.addHotelId = result.getString( "add_hotelid" );
                    this.addUserId = result.getInt( "add_userid" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.updHotelId = result.getString( "upd_hotelid" );
                    this.updUserId = result.getInt( "upd_userid" );
                    this.urlDocomo = result.getString( "url_docomo" );
                    this.urlAu = result.getString( "url_au" );
                    this.urlSoftbank = result.getString( "url_softbank" );
                    this.urlSmart = result.getString( "url_smart" );
                    this.titleSmart = result.getString( "title_smart" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemInfo.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 更新情報履歴データ設定
     * 
     * @param result 更新情報履歴データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.dataType = result.getInt( "data_type" );
                this.id = result.getInt( "id" );
                this.dispIdx = result.getInt( "disp_idx" );
                this.dispFlag = result.getInt( "disp_flag" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.title = result.getString( "title" );
                this.linkUrlIn = result.getString( "link_url_in" );
                this.linkUrlOut = result.getString( "link_url_out" );
                this.urlStartDate = result.getInt( "url_start_date" );
                this.urlEndDate = result.getInt( "url_end_date" );
                this.memberOnly = result.getInt( "member_only" );
                this.msg1Title = result.getString( "msg1_title" );
                this.msg1 = result.getString( "msg1" );
                this.msg2Title = result.getString( "msg2_title" );
                this.msg2 = result.getString( "msg2" );
                this.msg3Title = result.getString( "msg3_title" );
                this.msg3 = result.getString( "msg3" );
                this.msg4Title = result.getString( "msg4_title" );
                this.msg4 = result.getString( "msg4" );
                this.msg5Title = result.getString( "msg5_title" );
                this.msg5 = result.getString( "msg5" );
                this.msg6Title = result.getString( "msg6_title" );
                this.msg6 = result.getString( "msg6" );
                this.msg7Title = result.getString( "msg7_title" );
                this.msg7 = result.getString( "msg7" );
                this.msg8Title = result.getString( "msg8_title" );
                this.msg8 = result.getString( "msg8" );
                this.addDate = result.getInt( "add_date" );
                this.addTime = result.getInt( "add_time" );
                this.addHotelId = result.getString( "add_hotelid" );
                this.addUserId = result.getInt( "add_userid" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.updHotelId = result.getString( "upd_hotelid" );
                this.updUserId = result.getInt( "upd_userid" );
                this.urlDocomo = result.getString( "url_docomo" );
                this.urlAu = result.getString( "url_au" );
                this.urlSoftbank = result.getString( "url_Softbank" );
                this.urlSmart = result.getString( "url_smart" );
                this.titleSmart = result.getString( "title_smart" );
            }
        }
        catch ( Exception e )
        {
        }

        return(true);
    }

    /**
     * 更新情報履歴データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        return insertData( false );
    }

    /**
     * 更新情報履歴データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(boolean isStg)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_system_info SET ";
        query = query + " data_type = ?,";
        query = query + " id = ?,";
        query = query + " disp_idx = ?,";
        query = query + " disp_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " title = ?,";
        query = query + " link_url_in = ?,";
        query = query + " link_url_out = ?,";
        query = query + " url_start_date = ?,";
        query = query + " url_end_date = ?,";
        query = query + " member_only = ?,";
        query = query + " msg1_title = ?,";
        query = query + " msg1 = ?,";
        query = query + " msg2_title = ?,";
        query = query + " msg2 = ?,";
        query = query + " msg3_title = ?,";
        query = query + " msg3 = ?,";
        query = query + " msg4_title = ?,";
        query = query + " msg4 = ?,";
        query = query + " msg5_title = ?,";
        query = query + " msg5 = ?,";
        query = query + " msg6_title = ?,";
        query = query + " msg6 = ?,";
        query = query + " msg7_title = ?,";
        query = query + " msg7 = ?,";
        query = query + " msg8_title = ?,";
        query = query + " msg8 = ?,";
        query = query + " add_date = ?,";
        query = query + " add_time = ?,";
        query = query + " add_hotelid = ?,";
        query = query + " add_userid = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " upd_hotelid = ?,";
        query = query + " upd_userid = ?,";
        query = query + " url_docomo = ?,";
        query = query + " url_au = ?,";
        query = query + " url_softbank = ?,";
        query = query + " url_smart = ?,";
        query = query + " title_smart = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.dataType );
            prestate.setInt( 2, this.id );
            prestate.setInt( 3, this.dispIdx );
            prestate.setInt( 4, this.dispFlag );
            prestate.setInt( 5, this.startDate );
            prestate.setInt( 6, this.endDate );
            prestate.setString( 7, this.title );
            prestate.setString( 8, this.linkUrlIn );
            prestate.setString( 9, this.linkUrlOut );
            prestate.setInt( 10, this.urlStartDate );
            prestate.setInt( 11, this.urlEndDate );
            prestate.setInt( 12, this.memberOnly );
            prestate.setString( 13, this.msg1Title );
            prestate.setString( 14, this.msg1 );
            prestate.setString( 15, this.msg2Title );
            prestate.setString( 16, this.msg2 );
            prestate.setString( 17, this.msg3Title );
            prestate.setString( 18, this.msg3 );
            prestate.setString( 19, this.msg4Title );
            prestate.setString( 20, this.msg4 );
            prestate.setString( 21, this.msg5Title );
            prestate.setString( 22, this.msg5 );
            prestate.setString( 23, this.msg6Title );
            prestate.setString( 24, this.msg6 );
            prestate.setString( 25, this.msg7Title );
            prestate.setString( 26, this.msg7 );
            prestate.setString( 27, this.msg8Title );
            prestate.setString( 28, this.msg8 );
            prestate.setInt( 29, this.addDate );
            prestate.setInt( 30, this.addTime );
            prestate.setString( 31, this.addHotelId );
            prestate.setInt( 32, this.addUserId );
            prestate.setInt( 33, this.lastUpdate );
            prestate.setInt( 34, this.lastUptime );
            prestate.setString( 35, this.updHotelId );
            prestate.setInt( 36, this.updUserId );
            prestate.setString( 37, this.urlDocomo );
            prestate.setString( 38, this.urlAu );
            prestate.setString( 39, this.urlSoftbank );
            prestate.setString( 40, this.urlSmart );
            prestate.setString( 41, this.titleSmart );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                // PubSubデータ同期
                // DBSync.publish( prestate, true, isStg );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemInfo.insertData] Exception=" + e.toString() );
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
     * 更新情報履歴データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param dataType 表示タイプ
     * @param id 管理番号
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int dataType, int id)
    {
        return updateData( dataType, id, false );
    }

    /**
     * 更新情報履歴データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param dataType 表示タイプ
     * @param id 管理番号
     * @param isStg ステージング環境更新
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int dataType, int id, boolean isStg)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_system_info SET ";
        query = query + " disp_idx = ?,";
        query = query + " disp_flag = ?,";
        query = query + " start_date = ?,";
        query = query + " end_date = ?,";
        query = query + " title = ?,";
        query = query + " link_url_in = ?,";
        query = query + " link_url_out = ?,";
        query = query + " url_start_date = ?,";
        query = query + " url_end_date = ?,";
        query = query + " member_only = ?,";
        query = query + " msg1_title = ?,";
        query = query + " msg1 = ?,";
        query = query + " msg2_title = ?,";
        query = query + " msg2 = ?,";
        query = query + " msg3_title = ?,";
        query = query + " msg3 = ?,";
        query = query + " msg4_title = ?,";
        query = query + " msg4 = ?,";
        query = query + " msg5_title = ?,";
        query = query + " msg5 = ?,";
        query = query + " msg6_title = ?,";
        query = query + " msg6 = ?,";
        query = query + " msg7_title = ?,";
        query = query + " msg7 = ?,";
        query = query + " msg8_title = ?,";
        query = query + " msg8 = ?,";
        query = query + " add_date = ?,";
        query = query + " add_time = ?,";
        query = query + " add_hotelid = ?,";
        query = query + " add_userid = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " upd_hotelid = ?,";
        query = query + " upd_userid = ?,";
        query = query + " url_docomo = ?,";
        query = query + " url_au = ?,";
        query = query + " url_softbank = ?,";
        query = query + " url_smart = ?,";
        query = query + " title_smart = ?";
        query = query + " WHERE data_type = ? AND id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.dispIdx );
            prestate.setInt( 2, this.dispFlag );
            prestate.setInt( 3, this.startDate );
            prestate.setInt( 4, this.endDate );
            prestate.setString( 5, this.title );
            prestate.setString( 6, this.linkUrlIn );
            prestate.setString( 7, this.linkUrlOut );
            prestate.setInt( 8, this.urlStartDate );
            prestate.setInt( 9, this.urlEndDate );
            prestate.setInt( 10, this.memberOnly );
            prestate.setString( 11, this.msg1Title );
            prestate.setString( 12, this.msg1 );
            prestate.setString( 13, this.msg2Title );
            prestate.setString( 14, this.msg2 );
            prestate.setString( 15, this.msg3Title );
            prestate.setString( 16, this.msg3 );
            prestate.setString( 17, this.msg4Title );
            prestate.setString( 18, this.msg4 );
            prestate.setString( 19, this.msg5Title );
            prestate.setString( 20, this.msg5 );
            prestate.setString( 21, this.msg6Title );
            prestate.setString( 22, this.msg6 );
            prestate.setString( 23, this.msg7Title );
            prestate.setString( 24, this.msg7 );
            prestate.setString( 25, this.msg8Title );
            prestate.setString( 26, this.msg8 );
            prestate.setInt( 27, this.addDate );
            prestate.setInt( 28, this.addTime );
            prestate.setString( 29, this.addHotelId );
            prestate.setInt( 30, this.addUserId );
            prestate.setInt( 31, this.lastUpdate );
            prestate.setInt( 32, this.lastUptime );
            prestate.setString( 33, this.updHotelId );
            prestate.setInt( 34, this.updUserId );
            prestate.setString( 35, this.urlDocomo );
            prestate.setString( 36, this.urlAu );
            prestate.setString( 37, this.urlSoftbank );
            prestate.setString( 38, this.urlSmart );
            prestate.setString( 39, this.titleSmart );
            prestate.setInt( 40, dataType );
            prestate.setInt( 41, id );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                // sql文取得
                // String sql = prestate.toString().split( ":", 2 )[1];

                // schema追加
                // sql = sql.replace( "UPDATE ", "UPDATE hotenavi." );

                // updateをreplace文に変える
                // sql = DBSync.updateToReplaceSQL( sql );

                // PubSubデータ同期
                // DBSync.publish( sql, isStg );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemInfo.updateData] Exception=" + e.toString() );
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
