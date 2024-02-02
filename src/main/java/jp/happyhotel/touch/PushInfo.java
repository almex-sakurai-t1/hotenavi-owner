package jp.happyhotel.touch;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataApCampaignMaster;
import jp.happyhotel.data.DataApUserCampaign;

/**
 * Push通知情報
 *
 * @author S.Tashiro
 * @version 1.00 2011/05/19
 */
public class PushInfo implements Serializable
{

    /**
     *
     */
    private static final long      serialVersionUID = -6199069863331936331L;
    // 端末情報データ
    private DataApCampaignMaster[] dacm             = null;
    private DataApUserCampaign[]   dauc             = null;

    //
    public DataApCampaignMaster[] getCampaignMaster()
    {
        return dacm;
    }

    public void setCampaignMaster(DataApCampaignMaster[] campaignMaster)
    {
        this.dacm = campaignMaster;
    }

    public DataApUserCampaign[] getUserCampaign()
    {
        return dauc;
    }

    public void setUserCampain(DataApUserCampaign[] userCampaign)
    {
        this.dauc = userCampaign;
    }

    /**
     * PUSH通知情報一覧取得
     *
     * @param userId
     * @return
     */
    public boolean getData(String userId, int apliKind)
    {
        boolean ret = false;

        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_campaign_master A";
        query += " INNER JOIN ap_user_campaign B ON A.campaign_id = B.campaign_id";
        query += " WHERE B.user_id = ?";
        query += " AND B.apli_kind = ?";
        query += " AND ( A.disp_from < ? OR ( A.disp_from = ? AND A.disp_from_time <= ?))";
        query += " AND ( A.disp_to > ? OR ( A.disp_to = ? AND A.disp_to_time >= ?)) ";
        query += " ORDER BY B.push_date DESC, B.push_time DESC";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            i=1;
            prestate.setString( i++, userId );
            prestate.setInt( i++, apliKind );
            prestate.setInt( i++, nowDate );
            prestate.setInt( i++, nowDate );
            prestate.setInt( i++, nowTime );
            prestate.setInt( i++, nowDate );
            prestate.setInt( i++, nowDate );
            prestate.setInt( i++, nowTime );
            result = prestate.executeQuery();

            i = 0;
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
                result.beforeFirst();

                this.dacm = new DataApCampaignMaster[count];
                this.dauc = new DataApUserCampaign[count];
                while( result.next() != false )
                {
                    this.dacm[i] = new DataApCampaignMaster();
                    this.dauc[i] = new DataApUserCampaign();
                    this.dacm[i].setData( result );
                    this.dauc[i].setData( result );
                    i++;
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[PushInfo.getData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

    /**
     * 未読PUSH通知情報取得
     *
     * @param userId
     * @param date
     * @param time
     * @return
     */
    public boolean getUnreadData(String userId, String date, String time)
    {
        boolean ret = false;

        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_campaign_master A";
        query += " INNER JOIN ap_user_campaign B ON A.campaign_id = B.campaign_id AND B.disp_date = 0";
        query += " AND push_date * 1000000 + push_time >= ? *  1000000 + ? AND apli_kind = 1";
        query += " WHERE B.user_id = ?";
        query += " AND ( disp_from < ? OR ( disp_from = ? AND disp_from_time <= ? )) ";
        query += " AND ( disp_to > ? OR ( disp_to = ? AND disp_to_time >= ? )) ";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            int nowDate = Integer.parseInt( DateEdit.getDate( 2 ) );
            int nowTime = Integer.parseInt( DateEdit.getTime( 1 ) );
            i=1;
            prestate.setInt( i++, Integer.parseInt( date ) );
            prestate.setInt( i++, Integer.parseInt( time ) );
            prestate.setString( i++, userId );
            prestate.setInt( i++, nowDate );
            prestate.setInt( i++, nowDate );
            prestate.setInt( i++, nowTime );
            prestate.setInt( i++, nowDate );
            prestate.setInt( i++, nowDate );
            prestate.setInt( i++, nowTime );
            result = prestate.executeQuery();
            i=0;
            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
                result.beforeFirst();

                this.dacm = new DataApCampaignMaster[count];
                this.dauc = new DataApUserCampaign[count];
                while( result.next() != false )
                {
                    this.dacm[i] = new DataApCampaignMaster();
                    this.dauc[i] = new DataApUserCampaign();
                    this.dacm[i].setData( result );
                    this.dauc[i].setData( result );
                    i++;
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[PushInfo.getUnreadData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * PUSH通知情報一覧取得
     *
     * @param userId
     * @return
     */
    public boolean getData(String userId, int cpId, int apliKind)
    {
        boolean ret = false;

        int i = 0;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM ap_campaign_master A";
        query += " INNER JOIN ap_user_campaign B ON A.campaign_id = B.campaign_id";
        query += " WHERE B.user_id = ?";
        query += " AND A.campaign_id = ?";
        query += " AND B.apli_kind = ?";
        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, cpId );
            prestate.setInt( 3, apliKind );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.last() != false )
                {
                    count = result.getRow();
                }
                result.beforeFirst();

                this.dacm = new DataApCampaignMaster[count];
                this.dauc = new DataApUserCampaign[count];
                if ( result.next() != false )
                {
                    this.dacm[i] = new DataApCampaignMaster();
                    this.dauc[i] = new DataApUserCampaign();
                    this.dacm[i].setData( result );
                    this.dauc[i].setData( result );

                    // 取得したデータを見たことにする。
                    if ( dauc[i].getDispDate() == 0 )
                    {
                        this.dauc[i].setDispDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                        this.dauc[i].setDispTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                        this.dauc[i].updateData( userId, cpId, apliKind );
                    }

                    i++;
                }
            }
            if ( count > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[PushInfo.getData()] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return ret;
    }

}
