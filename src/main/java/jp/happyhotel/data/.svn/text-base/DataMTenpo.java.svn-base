package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * M_tenpoVo.
 * 
 * @author tashiro-s1
 * @version 1.0
 */
public class DataMTenpo implements Serializable
{

    private String tenpoid;
    private String tenponame;
    private String tenponamekana;
    private String zipcode;
    private String prefname;
    private String address1;
    private String address2;

    private String address3;
    private String phone;
    private String footer;
    private int    issuemode;

    private int    nowwaitcount;
    private int    nowwaittime;
    private int    lastupdate;
    private int    lastuptime;

    public DataMTenpo()
    {
        tenpoid = "";
        tenponame = "";
        tenponamekana = "";
        zipcode = "";
        prefname = "";
        address1 = "";
        address2 = "";
        address3 = "";
        phone = "";
        footer = "";
        issuemode = 0;
        nowwaitcount = 0;
        nowwaittime = 0;
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

    public String getTenponame()
    {
        return tenponame;
    }

    public void setTenponame(String tenponame)
    {
        this.tenponame = tenponame;
    }

    public String getTenponamekana()
    {
        return tenponamekana;
    }

    public void setTenponamekana(String tenponamekana)
    {
        this.tenponamekana = tenponamekana;
    }

    public String getZipcode()
    {
        return zipcode;
    }

    public void setZipcode(String zipcode)
    {
        this.zipcode = zipcode;
    }

    public String getPrefname()
    {
        return prefname;
    }

    public void setPrefname(String prefname)
    {
        this.prefname = prefname;
    }

    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getAddress3()
    {
        return address3;
    }

    public void setAddress3(String address3)
    {
        this.address3 = address3;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getFooter()
    {
        return footer;
    }

    public void setFooter(String footer)
    {
        this.footer = footer;
    }

    public int getIssuemode()
    {
        return issuemode;
    }

    public void setIssuemode(int issuemode)
    {
        this.issuemode = issuemode;
    }

    public int getNowwaitcount()
    {
        return nowwaitcount;
    }

    public void setNowwaitcount(int nowwaitcount)
    {
        this.nowwaitcount = nowwaitcount;
    }

    public int getNowwaittime()
    {
        return nowwaittime;
    }

    public void setNowwaittime(int nowwaittime)
    {
        this.nowwaittime = nowwaittime;
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
     * 店舗マスタ情報取得
     * 
     * 
     * @param tenpoId 店舗ID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String tenpoId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM m_tenpo WHERE tenpoid = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, tenpoId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    tenpoid = result.getString( "tenpoid" );
                    tenponame = result.getString( "tenponame" );
                    tenponamekana = result.getString( "tenponamekana" );
                    zipcode = result.getString( "zipcode" );
                    prefname = result.getString( "prefname" );
                    address1 = result.getString( "address1" );
                    address2 = result.getString( "address2" );
                    address3 = result.getString( "address3" );
                    phone = result.getString( "phone" );
                    footer = result.getString( "footer" );
                    issuemode = result.getInt( "issuemode" );
                    nowwaitcount = result.getInt( "nowwaitcount" );
                    nowwaittime = result.getInt( "nowwaittime" );
                    lastupdate = result.getInt( "lastupdate" );
                    lastuptime = result.getInt( "lastuptime" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMTenpo.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( tenpoId.compareTo( this.tenpoid ) == 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * 店舗マスタ情報取得
     * 
     * @param result 店舗マスタ情報取得データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                tenpoid = result.getString( "tenpoid" );
                tenponame = result.getString( "tenponame" );
                tenponamekana = result.getString( "tenponamekana" );
                zipcode = result.getString( "zipcode" );
                prefname = result.getString( "prefname" );
                address1 = result.getString( "address1" );
                address2 = result.getString( "address2" );
                address3 = result.getString( "address3" );
                phone = result.getString( "phone" );
                footer = result.getString( "footer" );
                issuemode = result.getInt( "issuemode" );
                nowwaitcount = result.getInt( "nowwaitcount" );
                nowwaittime = result.getInt( "nowwaittime" );
                lastupdate = result.getInt( "lastupdate" );
                lastuptime = result.getInt( "lastuptime" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMTenpo.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * 店舗マスタ情報取得
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

        query = "INSERT m_tenpo SET ";
        query = query + " tenpoid = ?,";
        query = query + " tenponame = ?,";
        query = query + " tenponamekana = ?,";
        query = query + " zipcode = ?,";
        query = query + " prefname = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " address3 = ?,";
        query = query + " phone = ?,";
        query = query + " footer= ?,";
        query = query + " issuemode = ?,";
        query = query + " nowwaitcount = ?,";
        query = query + " nowwaittime = ?,";
        query = query + " lastupdate = ?, ";
        query = query + " lastuptime = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.tenpoid );
            prestate.setString( 2, this.tenponame );
            prestate.setString( 3, this.tenponamekana );
            prestate.setString( 4, this.zipcode );
            prestate.setString( 5, this.prefname );
            prestate.setString( 6, this.address1 );
            prestate.setString( 7, this.address2 );
            prestate.setString( 8, this.address3 );
            prestate.setString( 9, this.phone );
            prestate.setString( 10, this.footer );
            prestate.setInt( 11, this.issuemode );
            prestate.setInt( 12, this.nowwaitcount );
            prestate.setInt( 13, this.nowwaittime );
            prestate.setInt( 14, this.lastupdate );
            prestate.setInt( 15, this.lastuptime );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMTenpo.insertData] Exception=" + e.toString() );
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
     * 店舗マスタ情報取得
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param userId ユーザーID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(String tenpoId)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE m_tenpo SET ";
        query = query + " tenponame = ?,";
        query = query + " tenponamekana = ?,";
        query = query + " zipcode = ?,";
        query = query + " prefname = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " address3 = ?,";
        query = query + " phone = ?,";
        query = query + " footer= ?,";
        query = query + " issuemode = ?,";
        query = query + " nowwaitcount = ?,";
        query = query + " nowwaittime = ?,";
        query = query + " lastupdate = ?, ";
        query = query + " lastuptime = ? ";
        query = query + " where tenpoid = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.tenponame );
            prestate.setString( 2, this.tenponamekana );
            prestate.setString( 3, this.zipcode );
            prestate.setString( 4, this.prefname );
            prestate.setString( 5, this.address1 );
            prestate.setString( 6, this.address2 );
            prestate.setString( 7, this.address3 );
            prestate.setString( 8, this.phone );
            prestate.setString( 9, this.footer );
            prestate.setInt( 10, this.issuemode );
            prestate.setInt( 11, this.nowwaitcount );
            prestate.setInt( 12, this.nowwaittime );
            prestate.setInt( 13, this.lastupdate );
            prestate.setInt( 14, this.lastuptime );
            prestate.setString( 15, tenpoId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMTenpo.updateData] Exception=" + e.toString() );
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
