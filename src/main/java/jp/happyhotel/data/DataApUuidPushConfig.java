package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;

/**
 * UUIDPUSH通知設定（ap_uuid_push_config）取得クラス
 *
 * @author an-j1
 * @version 1.00 2018/12/13
 */
public class DataApUuidPushConfig implements Serializable
{
    public static final String TABLE = "ap_uuid_push_config";
    private String             uuid;                             // UUID
    private int                pushFlag;                        // プッシュ通知全体設定（0:通知拒否、1:通知OK）
    private int                coFlag;                            // ハピホテからのお知らせ（0:通知拒否、1:通知OK）
    private int                campaignFlag;                 // ハピホテからのお知らせ（0:通知拒否、1:通知OK）
    private int                lastUpdate;                     // 最終更新日付（YYMMDD）
    private int                lastUptime;                     // 最終更新時刻（HHMMSS）

    /**
     * データを初期化します。
     */
    public DataApUuidPushConfig()
    {
        this.uuid = "";
        this.pushFlag = 0;
        this.coFlag = 0;
        this.campaignFlag = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;
    }

    public String getUuid()
    {
        return uuid;
    }

    public int getPushFlag()
    {
        return pushFlag;
    }

    public int getCoFlag()
    {
        return coFlag;
    }

    public int getCampaignFlag()
    {
        return campaignFlag;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public void setPushFlag(int pushFlag)
    {
        this.pushFlag = pushFlag;
    }

    public void setCoFlag(int coFlag)
    {
        this.coFlag = coFlag;
    }

    public void setCampaignFlag(int campaignFlag)
    {
        this.campaignFlag = campaignFlag;
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
     * UUIDPUSH通知設定（ap_uuid_push_config）取得
     *
     * @param uuid UUID
     * @return
     */
    public boolean getData(String uuid)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_uuid_push_config WHERE uuid = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, uuid );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.uuid = result.getString( "uuid" );
                    this.pushFlag = result.getInt( "push_flag" );
                    this.coFlag = result.getInt( "co_flag" );
                    this.campaignFlag = result.getInt( "campaign_flag" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuidPushConfig.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * UUIDPUSH通知設定（ap_uuid_push_config）設定
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
                this.uuid = result.getString( "uuid" );
                this.pushFlag = result.getInt( "push_flag" );
                this.coFlag = result.getInt( "co_flag" );
                this.campaignFlag = result.getInt( "campaign_flag" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuidPushConfig.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * UUIDPUSH通知設定（ap_uuid_push_config）挿入
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

        query = "INSERT ap_uuid_push_config SET ";
        query += " uuid=?";
        query += ", push_flag=?";
        query += ", co_flag=?";
        query += ", campaign_flag=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.uuid );
            prestate.setInt( i++, this.pushFlag );
            prestate.setInt( i++, this.coFlag );
            prestate.setInt( i++, this.campaignFlag );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getDate( 2 ) ) );
            prestate.setInt( i++, Integer.parseInt( DateEdit.getTime( 1 ) ) );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuidPushConfig.insertData] Exception=" + e.toString() );
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
     * UUIDPUSH通知設定（ap_uuid_push_config）更新
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param uuid UUID
     * @return
     */
    public boolean updateData(String uuid)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_uuid_push_config SET ";
        query += " push_flag=?";
        query += ", co_flag=?";
        query += ", campaign_flag=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE uuid=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.pushFlag );
            prestate.setInt( i++, this.coFlag );
            prestate.setInt( i++, this.campaignFlag );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setString( i++, this.uuid );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUuidPushConfig.updateData] Exception=" + e.toString() );
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
