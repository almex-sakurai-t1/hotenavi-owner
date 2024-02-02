package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ユーザPUSH通知設定（ap_user_push_config）取得クラス
 * 
 * @author Shingo Tashiro
 * @version 1.00 2014/10/22
 */
public class DataApUserPushConfig implements Serializable
{
    public static final String TABLE = "ap_user_push_config";
    private String             userId;                       // ユーザID
    private int                pushFlag;                     // プッシュ通知全体設定（0:通知OK、1:通知拒否）
    private int                coFlag;                       // チェックアウト通知（0:通知OK、1:通知拒否）
    private int                campaignFlag;                 // ハピホテからのお知らせ（0:通知OK、1:通知拒否）
    private int                lastUpdate;                   // 最終更新日付（YYMMDD）
    private int                lastUptime;                   // 最終更新時刻（HHMMSS）

    /**
     * データを初期化します。
     */
    public DataApUserPushConfig()
    {
        this.userId = "";
        this.pushFlag = 1;
        this.coFlag = 1;
        this.campaignFlag = 1;
        this.lastUpdate = 0;
        this.lastUptime = 0;
    }

    public String getUserId()
    {
        return userId;
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

    public void setUserId(String userId)
    {
        this.userId = userId;
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
     * ユーザPUSH通知設定（ap_user_push_config）取得
     * 
     * @param userId ユーザID
     * @return
     */
    public boolean getData(String userId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_user_push_config WHERE user_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
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
            Logging.error( "[DataApUserPushConfig.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザPUSH通知設定（ap_user_push_config）設定
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
                this.userId = result.getString( "user_id" );
                this.pushFlag = result.getInt( "push_flag" );
                this.coFlag = result.getInt( "co_flag" );
                this.campaignFlag = result.getInt( "campaign_flag" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserPushConfig.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ユーザPUSH通知設定（ap_user_push_config）挿入
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

        query = "INSERT ap_user_push_config SET ";
        query += " user_id=?";
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
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.pushFlag );
            prestate.setInt( i++, this.coFlag );
            prestate.setInt( i++, this.campaignFlag );
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
            Logging.error( "[DataApUserPushConfig.insertData] Exception=" + e.toString() );
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
     * ユーザPUSH通知設定（ap_user_push_config）更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザID
     * @return
     */
    public boolean updateData(String userId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_user_push_config SET ";
        query += " push_flag=?";
        query += ", co_flag=?";
        query += ", campaign_flag=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE user_id=?";

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
            prestate.setString( i++, this.userId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserPushConfig.updateData] Exception=" + e.toString() );
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
