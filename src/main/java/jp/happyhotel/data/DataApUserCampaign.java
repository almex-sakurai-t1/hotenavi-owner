package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * キャンペーン情報閲覧状況(ap_user_campaign)取得クラス
 *
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApUserCampaign implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -6534705698008726426L;
    public static final String TABLE            = "ap_user_campaign";
    private String             userId;                                  // ユーザーID
    private int                campaignId;                              // キャンペーンID
    private int                apliKind;                                // 1:ハピホテアプリ,10:予約アプリ
    private int                pushDate;                                // 最終PUSH送信日(YYYYMMDD)
    private int                pushTime;                                // 最終PUSH送信時刻(HHMMSS)
    private int                mailDate;                                // 最終メール送信日(YYYYMMDD)
    private int                mailTime;                                // 最終メール送信時刻(HHMMSS)
    private int                dispDate;                                // 表示日付(YYYYMMDD)
    private int                dispTime;                                // 表示時刻(HHMMSS)

    /**
     * データを初期化します。
     */
    public DataApUserCampaign()
    {
        this.userId = "";
        this.campaignId = 0;
        this.apliKind = 0;
        this.pushDate = 0;
        this.pushTime = 0;
        this.mailDate = 0;
        this.mailTime = 0;
        this.dispDate = 0;
        this.dispTime = 0;
    }

    public String getUserId()
    {
        return userId;
    }

    public int getCampaignId()
    {
        return campaignId;
    }

    public int getApliKind()
    {
        return apliKind;
    }

    public int getPushDate()
    {
        return pushDate;
    }

    public int getPushTime()
    {
        return pushTime;
    }

    public int getMailDate()
    {
        return mailDate;
    }

    public int getMailTime()
    {
        return mailTime;
    }

    public int getDispDate()
    {
        return dispDate;
    }

    public int getDispTime()
    {
        return dispTime;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setCampaignId(int campaignId)
    {
        this.campaignId = campaignId;
    }

    public void setApliKind(int apliKind)
    {
        this.apliKind = apliKind;
    }

    public void setPushDate(int pushDate)
    {
        this.pushDate = pushDate;
    }

    public void setPushTime(int pushTime)
    {
        this.pushTime = pushTime;
    }

    public void setMailDate(int mailDate)
    {
        this.mailDate = mailDate;
    }

    public void setMailTime(int mailTime)
    {
        this.mailTime = mailTime;
    }

    public void setDispDate(int dispDate)
    {
        this.dispDate = dispDate;
    }

    public void setDispTime(int dispTime)
    {
        this.dispTime = dispTime;
    }

    /****
     * キャンペーン情報閲覧状況(ap_user_campaign)取得
     *
     * @param userId ユーザーID
     * @param campaignId キャンペーンID
     * @return
     */
    public boolean getData(String userId, int campaignId, int apliKind)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_user_campaign WHERE user_id = ? AND campaign_id = ? AND apli_kind = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, campaignId );
            prestate.setInt( 3, apliKind );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.campaignId = result.getInt( "campaign_id" );
                    this.apliKind = result.getInt( "apli_kind" );
                    this.pushDate = result.getInt( "push_date" );
                    this.pushTime = result.getInt( "push_time" );
                    this.mailDate = result.getInt( "mail_date" );
                    this.mailTime = result.getInt( "mail_time" );
                    this.dispDate = result.getInt( "disp_date" );
                    this.dispTime = result.getInt( "disp_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserCampaign.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * キャンペーン情報閲覧状況(ap_user_campaign)取得
     *
     * @param connection コネクション
     * @param userId ユーザーID
     * @param campaignId キャンペーンID
     * @return
     */
    public boolean getData(Connection connection, String userId, int campaignId, int apliKind)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_user_campaign WHERE user_id = ? AND campaign_id = ? AND apli_kind = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, campaignId );
            prestate.setInt( 3, apliKind );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.userId = result.getString( "user_id" );
                    this.campaignId = result.getInt( "campaign_id" );
                    this.apliKind = result.getInt( "apli_kind" );
                    this.pushDate = result.getInt( "push_date" );
                    this.pushTime = result.getInt( "push_time" );
                    this.mailDate = result.getInt( "mail_date" );
                    this.mailTime = result.getInt( "mail_time" );
                    this.dispDate = result.getInt( "disp_date" );
                    this.dispTime = result.getInt( "disp_time" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserCampaign.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * キャンペーン情報閲覧状況(ap_user_campaign)設定
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
                this.campaignId = result.getInt( "campaign_id" );
                this.apliKind = result.getInt( "apli_kind" );
                this.pushDate = result.getInt( "push_date" );
                this.pushTime = result.getInt( "push_time" );
                this.mailDate = result.getInt( "mail_date" );
                this.mailTime = result.getInt( "mail_time" );
                this.dispDate = result.getInt( "disp_date" );
                this.dispTime = result.getInt( "disp_time" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserCampaign.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * キャンペーン情報閲覧状況(ap_user_campaign)挿入
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

        query = "INSERT ap_user_campaign SET ";
        query += " user_id=?";
        query += ", campaign_id=?";
        query += ", apli_kind=?";
        query += ", push_date=?";
        query += ", push_time=?";
        query += ", mail_date=?";
        query += ", mail_time=?";
        query += ", disp_date=?";
        query += ", disp_time=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.campaignId );
            prestate.setInt( i++, this.apliKind );
            prestate.setInt( i++, this.pushDate );
            prestate.setInt( i++, this.pushTime );
            prestate.setInt( i++, this.mailDate );
            prestate.setInt( i++, this.mailTime );
            prestate.setInt( i++, this.dispDate );
            prestate.setInt( i++, this.dispTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserCampaign.insertData] Exception=" + e.toString() );
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
     * キャンペーン情報閲覧状況(ap_user_campaign)挿入
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData( Connection connection )
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT ap_user_campaign SET ";
        query += " user_id=?";
        query += ", campaign_id=?";
        query += ", apli_kind=?";
        query += ", push_date=?";
        query += ", push_time=?";
        query += ", mail_date=?";
        query += ", mail_time=?";
        query += ", disp_date=?";
        query += ", disp_time=?";
        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.campaignId );
            prestate.setInt( i++, this.apliKind );
            prestate.setInt( i++, this.pushDate );
            prestate.setInt( i++, this.pushTime );
            prestate.setInt( i++, this.mailDate );
            prestate.setInt( i++, this.mailTime );
            prestate.setInt( i++, this.dispDate );
            prestate.setInt( i++, this.dispTime );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserCampaign.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }
    
    /**
     * キャンペーン情報閲覧状況(ap_user_campaign)更新
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザーID
     * @param campaignId キャンペーンID
     * @return
     */
    public boolean updateData(String userId, int campaignId, int apliKind)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_user_campaign SET ";
        query += " push_date=?";
        query += ", push_time=?";
        query += ", mail_date=?";
        query += ", mail_time=?";
        query += ", disp_date=?";
        query += ", disp_time=?";
        query += " WHERE user_id=? AND campaign_id=? AND apli_kind=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.pushDate );
            prestate.setInt( i++, this.pushTime );
            prestate.setInt( i++, this.mailDate );
            prestate.setInt( i++, this.mailTime );
            prestate.setInt( i++, this.dispDate );
            prestate.setInt( i++, this.dispTime );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.campaignId );
            prestate.setInt( i++, this.apliKind );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserCampaign.updateData] Exception=" + e.toString() );
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
     * キャンペーン情報閲覧状況(ap_user_campaign)更新
     *
     * @see "値のセット後(setXXX)に行うこと"
     * @param connection コネクション
     * @param userId ユーザーID
     * @param campaignId キャンペーンID
     * @return
     */
    public boolean updateData(Connection connection, String userId, int campaignId, int apliKind)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_user_campaign SET ";
        query += " push_date=?";
        query += ", push_time=?";
        query += ", mail_date=?";
        query += ", mail_time=?";
        query += ", disp_date=?";
        query += ", disp_time=?";
        query += " WHERE user_id=? AND campaign_id=? AND apli_kind=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.pushDate );
            prestate.setInt( i++, this.pushTime );
            prestate.setInt( i++, this.mailDate );
            prestate.setInt( i++, this.mailTime );
            prestate.setInt( i++, this.dispDate );
            prestate.setInt( i++, this.dispTime );
            prestate.setString( i++, this.userId );
            prestate.setInt( i++, this.campaignId );
            prestate.setInt( i++, this.apliKind );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApUserCampaign.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }
}
