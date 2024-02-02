package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * T_waitingVo.
 * 
 * @author tashiro-s1
 * @version 1.0
 */
public class DataTWaiting implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 7656934588486772793L;
    private String            tenpoid;
    private int               managedate;
    private int               waitingcode;
    private int               receivetime;
    private int               callno;
    private int               waitingmode;
    private int               replacedate;
    private int               replacetime;
    private int               numguest;
    private int               holddate;
    private int               holdtime;
    private int               holdmode;
    private int               holdcount;
    private String            seattype;
    private int               waittime;
    private int               lastupdate;
    private int               lastuptime;

    /**
     * Constractor
     */
    public DataTWaiting()
    {
        tenpoid = "";
        managedate = 0;
        waitingcode = 0;
        receivetime = 0;
        callno = 0;
        waitingmode = 0;
        replacedate = 0;
        replacetime = 0;
        numguest = 0;
        holddate = 0;
        holdtime = 0;
        holdmode = 0;
        holdcount = 0;
        seattype = "";
        waittime = 0;
        lastupdate = 0;
        lastuptime = 0;

    }

    public String getTenpoid()
    {
        return tenpoid;
    }

    public void setTenpoid(String tenpoid)
    {
        this.tenpoid = tenpoid;
    }

    public int getManagedate()
    {
        return managedate;
    }

    public void setManagedate(int managedate)
    {
        this.managedate = managedate;
    }

    public int getWaitingcode()
    {
        return waitingcode;
    }

    public void setWaitingcode(int waitingcode)
    {
        this.waitingcode = waitingcode;
    }

    public int getReceivetime()
    {
        return receivetime;
    }

    public void setReceivetime(int receivetime)
    {
        this.receivetime = receivetime;
    }

    public int getCallno()
    {
        return callno;
    }

    public void setCallno(int callno)
    {
        this.callno = callno;
    }

    public int getWaitingmode()
    {
        return waitingmode;
    }

    public void setWaitingmode(int waitingmode)
    {
        this.waitingmode = waitingmode;
    }

    public int getReplacedate()
    {
        return replacedate;
    }

    public void setReplacedate(int replacedate)
    {
        this.replacedate = replacedate;
    }

    public int getReplacetime()
    {
        return replacetime;
    }

    public void setReplacetime(int replacetime)
    {
        this.replacetime = replacetime;
    }

    public int getNumguest()
    {
        return numguest;
    }

    public void setNumguest(int numguest)
    {
        this.numguest = numguest;
    }

    public int getHolddate()
    {
        return holddate;
    }

    public void setHolddate(int holddate)
    {
        this.holddate = holddate;
    }

    public int getHoldtime()
    {
        return holdtime;
    }

    public void setHoldtime(int holdtime)
    {
        this.holdtime = holdtime;
    }

    public int getHoldmode()
    {
        return holdmode;
    }

    public void setHoldmode(int holdmode)
    {
        this.holdmode = holdmode;
    }

    public int getHoldcount()
    {
        return holdcount;
    }

    public void setHoldcount(int holdcount)
    {
        this.holdcount = holdcount;
    }

    public String getSeattype()
    {
        return seattype;
    }

    public void setSeattype(String seattype)
    {
        this.seattype = seattype;
    }

    public int getWaittime()
    {
        return waittime;
    }

    public void setWaittime(int waittime)
    {
        this.waittime = waittime;
    }

    public int getLastupdate()
    {
        return lastupdate;
    }

    public void setLastupdate(int lastupdate)
    {
        this.lastupdate = lastupdate;
    }

    public int getLastuptime()
    {
        return lastuptime;
    }

    public void setLastuptime(int lastuptime)
    {
        this.lastuptime = lastuptime;
    }

    /***
     * ウェイティング情報取得
     * 
     * 
     * @param tenpoId 店舗ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String tenpoId, int manageDate, int waitingCode)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM t_waiting  WHERE tenpoid = ? AND managedate = ? AND waitingcode = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, tenpoId );
            prestate.setInt( 2, manageDate );
            prestate.setInt( 3, waitingCode );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.tenpoid = result.getString( "tenpoid" );
                    this.managedate = result.getInt( "managedate" );
                    this.waitingcode = result.getInt( "waitingcode" );
                    this.receivetime = result.getInt( "receivetime" );
                    this.callno = result.getInt( "callno" );
                    this.waitingmode = result.getInt( "waitingmode" );
                    this.replacedate = result.getInt( "replacedate" );
                    this.replacetime = result.getInt( "replacetime" );
                    this.numguest = result.getInt( "numguest" );
                    this.holddate = result.getInt( "holddate" );
                    this.holdtime = result.getInt( "holdtime" );
                    this.holdmode = result.getInt( "holdmode" );
                    this.holdcount = result.getInt( "holdcount" );
                    this.seattype = result.getString( "seattype" );
                    this.waittime = result.getInt( "waittime" );
                    this.lastupdate = result.getInt( "lastupdate" );
                    this.lastuptime = result.getInt( "lastuptime" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataTWaitiong.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( this.tenpoid.equals( "" ) == false )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * ウェイティング情報取得
     * 
     * @param result ウェイティング情報取得データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                tenpoid = result.getString( "tenpoid" );
                managedate = result.getInt( "managedate" );
                waitingcode = result.getInt( "waitingcode" );
                receivetime = result.getInt( "receivetime" );
                callno = result.getInt( "callno" );
                waitingmode = result.getInt( "waitingmode" );
                replacedate = result.getInt( "replacedate" );
                replacetime = result.getInt( "replacetime" );
                numguest = result.getInt( "numguest" );
                holddate = result.getInt( "holddate" );
                holdtime = result.getInt( "holdtime" );
                holdmode = result.getInt( "holdmode" );
                holdcount = result.getInt( "holdcount" );
                seattype = result.getString( "seattype" );
                waittime = result.getInt( "waittime" );
                lastupdate = result.getInt( "lastupdate" );
                lastuptime = result.getInt( "lastuptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataTWaitiong.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ウェイティング情報取得
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

        query = "INSERT t_waiting  SET ";
        query = query + " tenpoid = ?,";
        query = query + " managedate = ?,";
        query = query + " waitingcode = ?,";
        query = query + " receivetime = ?,";
        query = query + " callno = ?,";
        query = query + " waitingmode = ?,";
        query = query + " replacedate = ?,";
        query = query + " replacetime = ?,";
        query = query + " numguest = ?,";
        query = query + " holddate = ?,";
        query = query + " holdtime = ?,";
        query = query + " holdmode = ?,";
        query = query + " holdcount = ?,";
        query = query + " seattype = ?,";
        query = query + " waittime = ?,";
        query = query + " lastupdate = ?,";
        query = query + " lastuptime = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.tenpoid );
            prestate.setInt( 2, this.managedate );
            prestate.setInt( 3, this.waitingcode );
            prestate.setInt( 4, this.receivetime );
            prestate.setInt( 5, this.callno );
            prestate.setInt( 6, this.waitingmode );
            prestate.setInt( 7, this.replacedate );
            prestate.setInt( 8, this.replacetime );
            prestate.setInt( 9, this.numguest );
            prestate.setInt( 10, this.holddate );
            prestate.setInt( 11, this.holdtime );
            prestate.setInt( 12, this.holdmode );
            prestate.setInt( 13, this.holdcount );
            prestate.setString( 14, this.seattype );
            prestate.setInt( 15, this.waittime );
            prestate.setInt( 16, this.lastupdate );
            prestate.setInt( 17, this.lastuptime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataTWaitiong.insertData] Exception=" + e.toString() );
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
     * ウェイティング情報取得
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String tenpoId, int manageDate, int waitingCode)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE t_waiting SET ";
        query = query + " receivetime = ?,";
        query = query + " callno = ?,";
        query = query + " waitingmode = ?,";
        query = query + " replacedate = ?,";
        query = query + " replacetime = ?,";
        query = query + " numguest = ?,";
        query = query + " holddate = ?,";
        query = query + " holdtime = ?,";
        query = query + " holdmode = ?,";
        query = query + " holdcount = ?,";
        query = query + " seattype = ?,";
        query = query + " waittime = ?,";
        query = query + " lastupdate = ?,";
        query = query + " lastuptime = ?";
        query = query + " where tenpoid = ?";
        query = query + " AND managedate = ?";
        query = query + " AND waitingcode = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.receivetime );
            prestate.setInt( 2, this.callno );
            prestate.setInt( 3, this.waitingmode );
            prestate.setInt( 4, this.replacedate );
            prestate.setInt( 5, this.replacetime );
            prestate.setInt( 6, this.numguest );
            prestate.setInt( 7, this.holddate );
            prestate.setInt( 8, this.holdtime );
            prestate.setInt( 9, this.holdmode );
            prestate.setInt( 10, this.holdcount );
            prestate.setString( 11, this.seattype );
            prestate.setInt( 12, this.waittime );
            prestate.setInt( 13, this.lastupdate );
            prestate.setInt( 14, this.lastuptime );

            prestate.setString( 15, tenpoId );
            prestate.setInt( 16, manageDate );
            prestate.setInt( 17, waitingCode );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataTWaitiong.updateData] Exception=" + e.toString() );
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
