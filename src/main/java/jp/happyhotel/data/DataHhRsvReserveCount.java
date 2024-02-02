package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * デバイス別予約件数データ取得クラス
 * 
 * @author Mitsuhashi
 * @version 1.00 2019/1/28
 */
public class DataHhRsvReserveCount implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -1191773740511683295L;
    public static final String TABLE            = "hh_rsv_reserve_count";
    /** 予約日: YYYYMMDD */
    private int                rsvDate;
    /** 基準フラグ 0：予約申込み日基準、1：予約日基準 */
    private int                rsvFlag;
    /** PCから予約カウント */
    private int                pcCount;
    /** スマホWEBから予約カウント */
    private int                smartCount;
    /** 予約アプリ(Android)から予約カウント */
    private int                rsvAppliAdrCount;
    /** 予約アプリ(iOS)から予約カウント */
    private int                rsvAppliIosCount;
    /** ハピホテアプリ(Android)から予約カウント */
    private int                happyAppliAdrCount;
    /** ハピホテアプリ(iOS)から予約カウント */
    private int                happyAppliIosCount;
    /** ラブインジャパンから予約カウント */
    private int                lijCount;
    /** OTAから予約カウント */
    private int                otaCount;
    /** PCから予約カウント */
    private int                pcCancelCount;
    /** スマホWEBから予約キャンセルカウント */
    private int                smartCancelCount;
    /** 予約アプリ(Android)から予約キャンセルカウント */
    private int                rsvAppliAdrCancelCount;
    /** 予約アプリ(iOS)から予約キャンセルカウント */
    private int                rsvAppliIosCancelCount;
    /** ハピホテアプリ(Android)から予約キャンセルカウント */
    private int                happyAppliAdrCancelCount;
    /** ハピホテアプリ(iOS)から予約キャンセルカウント */
    private int                happyAppliIosCancelCount;
    /** ラブインジャパンから予約キャンセルカウント */
    private int                lijCancelCount;
    /** OTAから予約カウント */
    private int                otaCancelCount;
    /** 最終更新日: YYYYMMDD */
    private int                lastUpdate;
    /** 最終更新時刻: HHMMSS */
    private int                lastUptime;

    /**
     * データを初期化します。
     */
    public DataHhRsvReserveCount()
    {
        this.rsvDate = 0;
        this.rsvFlag = 0;
        this.pcCount = 0;
        this.smartCount = 0;
        this.rsvAppliAdrCount = 0;
        this.rsvAppliIosCount = 0;
        this.happyAppliAdrCount = 0;
        this.happyAppliIosCount = 0;
        this.lijCount = 0;
        this.otaCount = 0;
        this.pcCancelCount = 0;
        this.smartCancelCount = 0;
        this.rsvAppliAdrCancelCount = 0;
        this.rsvAppliIosCancelCount = 0;
        this.happyAppliAdrCancelCount = 0;
        this.happyAppliIosCancelCount = 0;
        this.lijCancelCount = 0;
        this.otaCancelCount = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;
    }

    public int geRsvDate()
    {
        return rsvDate;
    }

    public int getRsvFlag()
    {
        return rsvFlag;
    }

    public int getPcCount()
    {
        return pcCount;
    }

    public int getSmartCount()
    {
        return smartCount;
    }

    public int getRsvAppliAdrCount()
    {
        return rsvAppliAdrCount;
    }

    public int getRsvAppliIosCount()
    {
        return rsvAppliIosCount;
    }

    public int getHappyAppliAdrCount()
    {
        return happyAppliAdrCount;
    }

    public int getHappyAppliIosCount()
    {
        return happyAppliIosCount;
    }

    public int getLijCount()
    {
        return lijCount;
    }

    public int getOtaCount()
    {
        return otaCount;
    }

    public int getPcCancelCount()
    {
        return pcCancelCount;
    }

    public int getSmartCancelCount()
    {
        return smartCancelCount;
    }

    public int getRsvAppliAdrCancelCount()
    {
        return rsvAppliAdrCancelCount;
    }

    public int getRsvAppliIosCancelCount()
    {
        return rsvAppliIosCancelCount;
    }

    public int getHappyAppliAdrCancelCount()
    {
        return happyAppliAdrCancelCount;
    }

    public int getHappyAppliIosCancelCount()
    {
        return happyAppliIosCancelCount;
    }

    public int getLijCancelCount()
    {
        return lijCancelCount;
    }

    public int getOtaCancelCount()
    {
        return otaCancelCount;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setRsvDate(int rsvDate)
    {
        this.rsvDate = rsvDate;
    }

    public void setRsvFlag(int rsvFlag)
    {
        this.rsvFlag = rsvFlag;
    }

    public void setPcCount(int pcCount)
    {
        this.pcCount = pcCount;
    }

    public void setSmartCount(int smartCount)
    {
        this.smartCount = smartCount;
    }

    public void setRsvAppliAdrCount(int rsvAppliAdrCount)
    {
        this.rsvAppliAdrCount = rsvAppliAdrCount;
    }

    public void setRsvAppliIosCount(int rsvAppliIosCount)
    {
        this.rsvAppliIosCount = rsvAppliIosCount;
    }

    public void setHappyAppliAdrCount(int happyAppliAdrCount)
    {
        this.happyAppliAdrCount = happyAppliAdrCount;
    }

    public void setHappyAppliIosCount(int happyAppliIosCount)
    {
        this.happyAppliIosCount = happyAppliIosCount;
    }

    public void setLijCount(int lijCount)
    {
        this.lijCount = lijCount;
    }

    public void setotaCount(int otaCount)
    {
        this.otaCount = otaCount;
    }

    public void setPcCancelCount(int pcCancelCount)
    {
        this.pcCancelCount = pcCancelCount;
    }

    public void setSmartCancelCount(int smartCancelCount)
    {
        this.smartCancelCount = smartCancelCount;
    }

    public void setRsvAppliAdrCancelCount(int rsvAppliAdrCancelCount)
    {
        this.rsvAppliAdrCancelCount = rsvAppliAdrCancelCount;
    }

    public void setRsvAppliIosCancelCount(int rsvAppliIosCancelCount)
    {
        this.rsvAppliIosCancelCount = rsvAppliIosCancelCount;
    }

    public void setHappyAppliAdrCancelCount(int happyAppliAdrCancelCount)
    {
        this.happyAppliAdrCancelCount = happyAppliAdrCancelCount;
    }

    public void setHappyAppliIosCancelCount(int happyAppliIosCancelCount)
    {
        this.happyAppliIosCancelCount = happyAppliIosCancelCount;
    }

    public void setLijCancelCount(int lijCancelCount)
    {
        this.lijCancelCount = lijCancelCount;
    }

    public void setotaCancelCount(int otaCancelCount)
    {
        this.otaCancelCount = otaCancelCount;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    /**
     * デバイス別予約件数データ取得
     * 
     * @param rsvDate 予約日
     * @param rsvFlag 基準フラグ　 0：予約申込み日基準、1：予約日基準
     * @return
     */
    public boolean getData(int rsvDate, int rsvFlag)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        int i = 1;
        query = "SELECT * FROM newRsvDB.hh_rsv_reserve_count WHERE rsv_date = ? AND rsv_flag = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( i++, rsvDate );
            prestate.setInt( i++, rsvFlag );
            result = prestate.executeQuery();
            if ( result.next() == false )
            {
                return false;
            }
            return setData( result );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveCount.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * デバイス別予約件数データ取得
     * 
     * @param connection Connection
     * @param rsvDate 予約日
     * @param rsvFlag 基準フラグ　 0：予約申込み日基準、1：予約日基準
     * @return
     */
    public boolean getData(Connection connection, int rsvDate, int rsvFlag)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int i = 1;
        ret = false;
        query = "SELECT * FROM newRsvDB.hh_rsv_reserve_count WHERE rsv_date = ? AND rsv_flag = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( i++, rsvDate );
            prestate.setInt( i++, rsvFlag );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                return setData( result );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveCount.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * デバイス別予約件数データ設定
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
                this.rsvDate = result.getInt( "rsv_date" );
                this.rsvFlag = result.getInt( "rsv_flag" );
                this.pcCount = result.getInt( "pc_count" );
                this.smartCount = result.getInt( "smart_count" );
                this.rsvAppliAdrCount = result.getInt( "rsv_appli_adr_count" );
                this.rsvAppliIosCount = result.getInt( "rsv_appli_ios_count" );
                this.happyAppliAdrCount = result.getInt( "happy_appli_adr_count" );
                this.happyAppliIosCount = result.getInt( "happy_appli_ios_count" );
                this.lijCount = result.getInt( "lij_count" );
                this.otaCount = result.getInt( "ota_count" );
                this.pcCancelCount = result.getInt( "pc_cancel_count" );
                this.smartCancelCount = result.getInt( "smart_cancel_count" );
                this.rsvAppliAdrCancelCount = result.getInt( "rsv_appli_adr_cancel_count" );
                this.rsvAppliIosCancelCount = result.getInt( "rsv_appli_ios_cancel_count" );
                this.happyAppliAdrCancelCount = result.getInt( "happy_appli_adr_cancel_count" );
                this.happyAppliIosCancelCount = result.getInt( "happy_appli_ios_cancel_count" );
                this.lijCancelCount = result.getInt( "lij_cancel_count" );
                this.otaCancelCount = result.getInt( "ota_cancel_count" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvResreveCount.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * デバイス別予約件数データ挿入
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

        query = "INSERT newRsvDB.hh_rsv_reserve_count SET ";
        query += " rsv_date=?";
        query += ", rsv_flag=?";
        query += ", pc_count=?";
        query += ", smart_count=?";
        query += ", rsv_appli_adr_count=?";
        query += ", rsv_appli_ios_count=?";
        query += ", happy_appli_adr_count=?";
        query += ", happy_appli_ios_count=?";
        query += ", lij_count=?";
        query += ", ota_count=?";
        query += ", pc_cancel_count=?";
        query += ", smart_cancel_count=?";
        query += ", rsv_appli_adr_cancel_count=?";
        query += ", rsv_appli_ios_cancel_count=?";
        query += ", happy_appli_adr_cancel_count=?";
        query += ", happy_appli_ios_cancel_count=?";
        query += ", lij_cancel_count=?";
        query += ", ota_cancel_count=?";
        query += ", last_update=?";
        query += ", last_uptime=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.rsvDate );
            prestate.setInt( i++, this.rsvFlag );
            prestate.setInt( i++, this.pcCount );
            prestate.setInt( i++, this.smartCount );
            prestate.setInt( i++, this.rsvAppliAdrCount );
            prestate.setInt( i++, this.rsvAppliIosCount );
            prestate.setInt( i++, this.happyAppliAdrCount );
            prestate.setInt( i++, this.happyAppliIosCount );
            prestate.setInt( i++, this.lijCount );
            prestate.setInt( i++, this.otaCount );
            prestate.setInt( i++, this.pcCancelCount );
            prestate.setInt( i++, this.smartCancelCount );
            prestate.setInt( i++, this.rsvAppliAdrCancelCount );
            prestate.setInt( i++, this.rsvAppliIosCancelCount );
            prestate.setInt( i++, this.happyAppliAdrCancelCount );
            prestate.setInt( i++, this.happyAppliIosCancelCount );
            prestate.setInt( i++, this.lijCancelCount );
            prestate.setInt( i++, this.otaCancelCount );
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
            Logging.error( "[DataHhRsvReserveCount.insertData] Exception=" + e.toString() );
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
     * デバイス別予約件数データ挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT newRsvDB.hh_rsv_reserve_count SET ";
        query += "  rsv_date=?";
        query += ", rsv_flag=?";
        query += ", pc_count=?";
        query += ", smart_count=?";
        query += ", rsv_appli_adr_count=?";
        query += ", rsv_appli_ios_count=?";
        query += ", happy_appli_adr_count=?";
        query += ", happy_appli_ios_count=?";
        query += ", lij_count=?";
        query += ", ota_count=?";
        query += ", pc_cancel_count=?";
        query += ", smart_cancel_count=?";
        query += ", rsv_appli_adr_cancel_count=?";
        query += ", rsv_appli_ios_cancel_count=?";
        query += ", happy_appli_adr_cancel_count=?";
        query += ", happy_appli_ios_cancel_count=?";
        query += ", lij_cancel_count=?";
        query += ", ota_cancel_count=?";
        query += ", last_update=?";
        query += ", last_uptime=?";

        try
        {
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.rsvDate );
            prestate.setInt( i++, this.rsvFlag );
            prestate.setInt( i++, this.pcCount );
            prestate.setInt( i++, this.smartCount );
            prestate.setInt( i++, this.rsvAppliAdrCount );
            prestate.setInt( i++, this.rsvAppliIosCount );
            prestate.setInt( i++, this.happyAppliAdrCount );
            prestate.setInt( i++, this.happyAppliIosCount );
            prestate.setInt( i++, this.lijCount );
            prestate.setInt( i++, this.otaCount );
            prestate.setInt( i++, this.pcCancelCount );
            prestate.setInt( i++, this.smartCancelCount );
            prestate.setInt( i++, this.rsvAppliAdrCancelCount );
            prestate.setInt( i++, this.rsvAppliIosCancelCount );
            prestate.setInt( i++, this.happyAppliAdrCancelCount );
            prestate.setInt( i++, this.happyAppliIosCancelCount );
            prestate.setInt( i++, this.lijCancelCount );
            prestate.setInt( i++, this.otaCancelCount );
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
            Logging.error( "[DataHhRsvReserveCount.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * 予デバイス別予約件数データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param reserveNo 予約番号
     * @return
     */
    public boolean updateData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_reserve_count SET ";
        query += " pc_count=?";
        query += ", smart_count=?";
        query += ", rsv_appli_adr_count=?";
        query += ", rsv_appli_ios_count=?";
        query += ", happy_appli_adr_count=?";
        query += ", happy_appli_ios_count=?";
        query += ", lij_count=?";
        query += ", ota_count=?";
        query += ", pc_cancel_count=?";
        query += ", smart_cancel_count=?";
        query += ", rsv_appli_adr_cancel_count=?";
        query += ", rsv_appli_ios_cancel_count=?";
        query += ", happy_appli_adr_cancel_count=?";
        query += ", happy_appli_ios_cancel_count=?";
        query += ", lij_cancel_count=?";
        query += ", ota_cancel_count=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE rsv_date=?";
        query += " AND rsv_flag=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.pcCount );
            prestate.setInt( i++, this.smartCount );
            prestate.setInt( i++, this.rsvAppliAdrCount );
            prestate.setInt( i++, this.rsvAppliIosCount );
            prestate.setInt( i++, this.happyAppliAdrCount );
            prestate.setInt( i++, this.happyAppliIosCount );
            prestate.setInt( i++, this.lijCount );
            prestate.setInt( i++, this.otaCount );
            prestate.setInt( i++, this.pcCancelCount );
            prestate.setInt( i++, this.smartCancelCount );
            prestate.setInt( i++, this.rsvAppliAdrCancelCount );
            prestate.setInt( i++, this.rsvAppliIosCancelCount );
            prestate.setInt( i++, this.happyAppliAdrCancelCount );
            prestate.setInt( i++, this.happyAppliIosCancelCount );
            prestate.setInt( i++, this.lijCancelCount );
            prestate.setInt( i++, this.otaCancelCount );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.rsvDate );
            prestate.setInt( i++, this.rsvFlag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveCount.updateData] Exception=" + e.toString() );
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
     * デバイス別予約件数データ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param reserveNo 予約番号
     * @return
     */
    public boolean updateData(Connection connection)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE newRsvDB.hh_rsv_reserve_count SET ";
        query += " pc_count=?";
        query += ", smart_count=?";
        query += ", rsv_appli_adr_count=?";
        query += ", rsv_appli_ios_count=?";
        query += ", happy_appli_adr_count=?";
        query += ", happy_appli_ios_count=?";
        query += ", lij_count=?";
        query += ", ota_count=?";
        query += ", pc_cancel_count=?";
        query += ", smart_cancel_count=?";
        query += ", rsv_appli_adr_cancel_count=?";
        query += ", rsv_appli_ios_cancel_count=?";
        query += ", happy_appli_adr_cancel_count=?";
        query += ", happy_appli_ios_cancel_count=?";
        query += ", lij_cancel_count=?";
        query += ", ota_cancel_count=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += " WHERE rsv_date=?";
        query += " AND rsv_flag=?";

        try
        {
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.pcCount );
            prestate.setInt( i++, this.smartCount );
            prestate.setInt( i++, this.rsvAppliAdrCount );
            prestate.setInt( i++, this.rsvAppliIosCount );
            prestate.setInt( i++, this.happyAppliAdrCount );
            prestate.setInt( i++, this.happyAppliIosCount );
            prestate.setInt( i++, this.lijCount );
            prestate.setInt( i++, this.otaCount );
            prestate.setInt( i++, this.pcCancelCount );
            prestate.setInt( i++, this.smartCancelCount );
            prestate.setInt( i++, this.rsvAppliAdrCancelCount );
            prestate.setInt( i++, this.rsvAppliIosCancelCount );
            prestate.setInt( i++, this.happyAppliAdrCancelCount );
            prestate.setInt( i++, this.happyAppliIosCancelCount );
            prestate.setInt( i++, this.lijCancelCount );
            prestate.setInt( i++, this.otaCancelCount );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.rsvDate );
            prestate.setInt( i++, this.rsvFlag );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvReserveCount.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /****
     * デバイス別予約件数データ存在チェック
     * 
     * @param rsvDate 予約日
     * @param rsvFlag 基準フラグ　 0：予約申込み日基準、1：予約日基準
     * @return
     */
    public boolean existsData(int rsvDate, int rsvFlag, Connection connection)
    {
        boolean ret;
        String query;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT count(*) as cnt FROM newRsvDB.hh_rsv_reserve_count WHERE rsv_date = ? AND rsv_flag = ?";

        try
        {
            prestate = connection.prepareStatement( query );
            int i = 1;
            prestate.setInt( i++, rsvDate );
            prestate.setInt( i++, rsvFlag );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    if ( result.getInt( "cnt" ) > 0 )
                    {
                        ret = true;
                    }
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhRsvDayCharge.existsData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result );
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

}
