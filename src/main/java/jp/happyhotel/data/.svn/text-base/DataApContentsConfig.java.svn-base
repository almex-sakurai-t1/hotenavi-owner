package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * コンテンツコントロール（ap_contents_config）取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApContentsConfig implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = 4174691585403708340L;
    public static final String TABLE            = "ap_contents_config";
    private String             contentsId;                             // コンテンツ種類もしくはファイル名
    private int                contentsSub;                            // サブコード
    private int                id;                                     // ハピホテホテルID（0はDefault設定値登録用）
    private int                contentsSeq;                            // 連番
    private String             contents;                               // 読み込まれるコンテンツ内容
    private int                delFlag;                                // 1:表示しない
    private int                startDate;                              // 利用開始日付(YYYYMMDD)
    private int                startTime;                              // 利用開始時刻(HHMMSS)
    private int                endDate;                                // 利用終了日付(YYYYMMDD)
    private int                endTime;                                // 利用終了時刻(HHMMSS)
    private String             ownerHotelId;                           // 更新契約ホテルID
    private int                ownerUserId;                            // 更新オーナーユーザーID
    private int                lastUpdate;                             // 最終更新日付(YYYYMMDD)
    private int                lastUptime;                             // 最終更新時刻(HHMMSS)

    /**
     * データを初期化します。
     */
    public DataApContentsConfig()
    {
        this.contentsId = "";
        this.contentsSub = 0;
        this.id = 0;
        this.contentsSeq = 0;
        this.contents = "";
        this.delFlag = 0;
        this.startDate = 0;
        this.startTime = 0;
        this.endDate = 0;
        this.endTime = 0;
        this.ownerHotelId = "";
        this.ownerUserId = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;
    }

    public String getContentsId()
    {
        return contentsId;
    }

    public int getContentsSub()
    {
        return contentsSub;
    }

    public int getId()
    {
        return id;
    }

    public int getContentsSeq()
    {
        return contentsSeq;
    }

    public String getContents()
    {
        return contents;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public int getStartTime()
    {
        return startTime;
    }

    public int getEndDate()
    {
        return endDate;
    }

    public int getEndTime()
    {
        return endTime;
    }

    public String getOwnerHotelId()
    {
        return ownerHotelId;
    }

    public int getOwnerUserId()
    {
        return ownerUserId;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setContentsId(String contentsId)
    {
        this.contentsId = contentsId;
    }

    public void setContentsSub(int contentsSub)
    {
        this.contentsSub = contentsSub;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setContentsSeq(int contentsSeq)
    {
        this.contentsSeq = contentsSeq;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public void setStartTime(int startTime)
    {
        this.startTime = startTime;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public void setEndTime(int endTime)
    {
        this.endTime = endTime;
    }

    public void setOwnerHotelId(String ownerHotelId)
    {
        this.ownerHotelId = ownerHotelId;
    }

    public void setOwnerUserId(int ownerUserId)
    {
        this.ownerUserId = ownerUserId;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    /****
     * コンテンツコントロール（ap_contents_config）取得
     * 
     * @param contentsId コンテンツ種類もしくはファイル名
     * @param contentsSub サブコード
     * @param id ハピホテホテルID（0はDefault設定値登録用）
     * @param contentsSeq 連番
     * @return
     */
    public boolean getData(String contentsId, int contentsSub, int id, int contentsSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_contents_config WHERE contents_id = ? AND contents_sub = ? AND id = ? AND contents_seq = ? ";
        query += " ORDER BY id DESC ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, contentsId );
            prestate.setInt( 2, contentsSub );
            prestate.setInt( 3, id );
            prestate.setInt( 4, contentsSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.contentsId = result.getString( "contents_id" );
                    this.contentsSub = result.getInt( "contents_sub" );
                    this.id = result.getInt( "id" );
                    this.contentsSeq = result.getInt( "contents_seq" );
                    this.contents = result.getString( "contents" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.startTime = result.getInt( "start_time" );
                    this.endDate = result.getInt( "end_date" );
                    this.endTime = result.getInt( "end_time" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApContentsConfig.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * コンテンツコントロール（ap_contents_config）取得
     * 
     * @param contentsId コンテンツ種類もしくはファイル名
     * @param contentsSub サブコード
     * @param id ハピホテホテルID（0はDefault設定値登録用）
     * @param contentsSeq 連番
     * @return
     */
    public boolean getDataCommon(String contentsId, int contentsSub, int id, int contentsSeq)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_contents_config WHERE contents_id = ? AND contents_sub = ? AND id = ? AND contents_seq = ? ";
        query += " UNION SELECT * FROM ap_contents_config WHERE contents_id = ? AND contents_sub = ? AND id = ? AND contents_seq = ? ";
        query += " ORDER BY id DESC ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, contentsId );
            prestate.setInt( 2, contentsSub );
            prestate.setInt( 3, id );
            prestate.setInt( 4, contentsSeq );
            prestate.setString( 5, contentsId );
            prestate.setInt( 6, contentsSub );
            prestate.setInt( 7, 0 );
            prestate.setInt( 8, contentsSeq );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.contentsId = result.getString( "contents_id" );
                    this.contentsSub = result.getInt( "contents_sub" );
                    this.id = result.getInt( "id" );
                    this.contentsSeq = result.getInt( "contents_seq" );
                    this.contents = result.getString( "contents" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.startDate = result.getInt( "start_date" );
                    this.startTime = result.getInt( "start_time" );
                    this.endDate = result.getInt( "end_date" );
                    this.endTime = result.getInt( "end_time" );
                    this.ownerHotelId = result.getString( "owner_hotel_id" );
                    this.ownerUserId = result.getInt( "owner_user_id" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApContentsConfig.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * コンテンツコントロール（ap_contents_config）設定
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.contentsId = result.getString( "contents_id" );
                this.contentsSub = result.getInt( "contents_sub" );
                this.id = result.getInt( "id" );
                this.contentsSeq = result.getInt( "contents_seq" );
                this.contents = result.getString( "contents" );
                this.delFlag = result.getInt( "del_flag" );
                this.startDate = result.getInt( "start_date" );
                this.startTime = result.getInt( "start_time" );
                this.endDate = result.getInt( "end_date" );
                this.endTime = result.getInt( "end_time" );
                this.ownerHotelId = result.getString( "owner_hotel_id" );
                this.ownerUserId = result.getInt( "owner_user_id" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApContentsConfig.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * コンテンツコントロール（ap_contents_config）挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT ap_contents_config SET ";
        query += " contents_id=?";
        query += ", contents_sub=?";
        query += ", id=?";
        query += ", contents_seq=?";
        query += ", contents=?";
        query += ", del_flag=?";
        query += ", start_date=?";
        query += ", start_time=?";
        query += ", end_date=?";
        query += ", end_time=?";
        query += ", owner_hotel_id=?";
        query += ", owner_user_id=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.contentsId );
            prestate.setInt( i++, this.contentsSub );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.contentsSeq );
            prestate.setString( i++, this.contents );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.startTime );
            prestate.setInt( i++, this.endDate );
            prestate.setInt( i++, this.endTime );
            prestate.setString( i++, this.ownerHotelId );
            prestate.setInt( i++, this.ownerUserId );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApContentsConfig.insertData] Exception=" + e.toString() );
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
     * コンテンツコントロール（ap_contents_config）更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param contentsId コンテンツ種類もしくはファイル名
     * @param contentsSub サブコード
     * @param id ハピホテホテルID（0はDefault設定値登録用）
     * @param contentsSeq 連番
     * @return
     */
    public boolean updateData(String contentsId, int contentsSub, int id, int contentsSeq)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_contents_config SET ";
        query += " contents=?";
        query += ", del_flag=?";
        query += ", start_date=?";
        query += ", start_time=?";
        query += ", end_date=?";
        query += ", end_time=?";
        query += ", owner_hotel_id=?";
        query += ", owner_user_id=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE contents_id=? AND contents_sub=? AND id=? AND contents_seq=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.contents );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.startDate );
            prestate.setInt( i++, this.startTime );
            prestate.setInt( i++, this.endDate );
            prestate.setInt( i++, this.endTime );
            prestate.setString( i++, this.ownerHotelId );
            prestate.setInt( i++, this.ownerUserId );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setString( i++, this.contentsId );
            prestate.setInt( i++, this.contentsSub );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.contentsSeq );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApContentsConfig.updateData] Exception=" + e.toString() );
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
