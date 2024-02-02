package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri Dec 21 11:08:28 JST 2012
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * Hh_user_sp_tempVo.
 * 
 * @author tashiro-s1
 * @version 1.0
 *          history
 *          Symbol Date Person Note
 *          [1] 2012/12/21 tashiro-s1 Generated.
 */
public class DataUserSpTemp implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -8891598649639866421L;
    private String            openId;
    private String            mobileTermno;
    private String            orderNo;
    private String            siteCode;

    public DataUserSpTemp()
    {
        openId = "";
        mobileTermno = "";
        orderNo = "";
        siteCode = "";
    }

    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public String getMobileTermno()
    {
        return mobileTermno;
    }

    public void setMobileTermno(String mobileTermno)
    {
        this.mobileTermno = mobileTermno;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getSiteCode()
    {
        return siteCode;
    }

    public void setSiteCode(String siteCode)
    {
        this.siteCode = siteCode;
    }

    /**
     * ユーザスマホ課金情報取得
     * 
     * @param userId ユーザユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String openId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_sp_temp WHERE open_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, openId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.openId = result.getString( "open_id" );
                    this.mobileTermno = result.getString( "mobile_termno" );
                    this.orderNo = result.getString( "order_no" );
                    this.siteCode = result.getString( "site_code" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザスマホ課金情報取得
     * 
     * @param userId ユーザユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataBySuid(String uid)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_sp_temp WHERE mobile_termno = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, uid );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.openId = result.getString( "open_id" );
                    this.mobileTermno = result.getString( "mobile_termno" );
                    this.orderNo = result.getString( "order_no" );
                    this.siteCode = result.getString( "site_code" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザスマホ課金情報取得
     * 
     * @param userId ユーザユーザID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getDataByOrderNo(String orderNo)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM hh_user_sp_temp WHERE order_no = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, orderNo );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.openId = result.getString( "open_id" );
                    this.mobileTermno = result.getString( "mobile_termno" );
                    this.orderNo = result.getString( "order_no" );
                    this.siteCode = result.getString( "site_code" );

                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザスマホ課金データ設定
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.openId = result.getString( "open_id" );
                this.mobileTermno = result.getString( "mobile_termno" );
                this.orderNo = result.getString( "order_no" );
                this.siteCode = result.getString( "site_code" );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * ユーザスマホ課金データ追加
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

        query = "INSERT hh_user_sp_temp SET ";
        query = query + " open_id = ?,";
        query = query + " mobile_termno = ?,";
        query = query + " order_no = ?,";
        query = query + " site_code = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( 1, this.openId );
            prestate.setString( 2, this.mobileTermno );
            prestate.setString( 3, this.orderNo );
            prestate.setString( 4, this.siteCode );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserSp.insertData] Exception=" + e.toString() );
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
