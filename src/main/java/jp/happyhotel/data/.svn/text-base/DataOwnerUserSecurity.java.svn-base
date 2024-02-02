/*
 * @(#)DataOwnerUserSecurity.java 1.00 2007/09/20 Copyright (C) ALMEX Inc. 2007 オーナーアクセス権情報取得クラス
 */
package jp.happyhotel.data;

import java.io.*;
import java.sql.*;

import jp.happyhotel.common.*;

/**
 * オーナーアクセス権情報取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/09/20
 * @version 1.1 2007/11/28
 */
public class DataOwnerUserSecurity implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -7916911316288868465L;

    private String            hotelId;
    private int               userId;
    private int               adminFlag;
    private int               secLevel01;
    private int               secLevel02;
    private int               secLevel03;
    private int               secLevel04;
    private int               secLevel05;
    private int               secLevel06;
    private int               secLevel07;
    private int               secLevel08;
    private int               secLevel09;
    private int               secLevel10;
    private int               secLevel11;
    private int               secLevel12;
    private int               secLevel13;
    private int               secLevel14;
    private int               secLevel15;
    private int               secLevel16;
    private int               secLevel17;
    private int               secLevel18;
    private int               secLevel19;
    private int               secLevel20;

    /**
     * データを初期化します。
     */
    public DataOwnerUserSecurity()
    {
        hotelId = "";
        userId = 0;
        adminFlag = 0;
        secLevel01 = 0;
        secLevel02 = 0;
        secLevel03 = 0;
        secLevel04 = 0;
        secLevel05 = 0;
        secLevel06 = 0;
        secLevel07 = 0;
        secLevel08 = 0;
        secLevel09 = 0;
        secLevel10 = 0;
        secLevel11 = 0;
        secLevel12 = 0;
        secLevel13 = 0;
        secLevel14 = 0;
        secLevel15 = 0;
        secLevel16 = 0;
        secLevel17 = 0;
        secLevel18 = 0;
        secLevel19 = 0;
        secLevel20 = 0;
    }

    public int getAdminFlag()
    {
        return adminFlag;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public int getSecLevel01()
    {
        return secLevel01;
    }

    public int getSecLevel02()
    {
        return secLevel02;
    }

    public int getSecLevel03()
    {
        return secLevel03;
    }

    public int getSecLevel04()
    {
        return secLevel04;
    }

    public int getSecLevel05()
    {
        return secLevel05;
    }

    public int getSecLevel06()
    {
        return secLevel06;
    }

    public int getSecLevel07()
    {
        return secLevel07;
    }

    public int getSecLevel08()
    {
        return secLevel08;
    }

    public int getSecLevel09()
    {
        return secLevel09;
    }

    public int getSecLevel10()
    {
        return secLevel10;
    }

    public int getSecLevel11()
    {
        return secLevel11;
    }

    public int getSecLevel12()
    {
        return secLevel12;
    }

    public int getSecLevel13()
    {
        return secLevel13;
    }

    public int getSecLevel14()
    {
        return secLevel14;
    }

    public int getSecLevel15()
    {
        return secLevel15;
    }

    public int getSecLevel16()
    {
        return secLevel16;
    }

    public int getSecLevel17()
    {
        return secLevel17;
    }

    public int getSecLevel18()
    {
        return secLevel18;
    }

    public int getSecLevel19()
    {
        return secLevel19;
    }

    public int getSecLevel20()
    {
        return secLevel20;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setAdminFlag(int adminFlag)
    {
        this.adminFlag = adminFlag;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setSecLevel01(int secLevel01)
    {
        this.secLevel01 = secLevel01;
    }

    public void setSecLevel02(int secLevel02)
    {
        this.secLevel02 = secLevel02;
    }

    public void setSecLevel03(int secLevel03)
    {
        this.secLevel03 = secLevel03;
    }

    public void setSecLevel04(int secLevel04)
    {
        this.secLevel04 = secLevel04;
    }

    public void setSecLevel05(int secLevel05)
    {
        this.secLevel05 = secLevel05;
    }

    public void setSecLevel06(int secLevel06)
    {
        this.secLevel06 = secLevel06;
    }

    public void setSecLevel07(int secLevel07)
    {
        this.secLevel07 = secLevel07;
    }

    public void setSecLevel08(int secLevel08)
    {
        this.secLevel08 = secLevel08;
    }

    public void setSecLevel09(int secLevel09)
    {
        this.secLevel09 = secLevel09;
    }

    public void setSecLevel10(int secLevel10)
    {
        this.secLevel10 = secLevel10;
    }

    public void setSecLevel11(int secLevel11)
    {
        this.secLevel11 = secLevel11;
    }

    public void setSecLevel12(int secLevel12)
    {
        this.secLevel12 = secLevel12;
    }

    public void setSecLevel13(int secLevel13)
    {
        this.secLevel13 = secLevel13;
    }

    public void setSecLevel14(int secLevel14)
    {
        this.secLevel14 = secLevel14;
    }

    public void setSecLevel15(int secLevel15)
    {
        this.secLevel15 = secLevel15;
    }

    public void setSecLevel16(int secLevel16)
    {
        this.secLevel16 = secLevel16;
    }

    public void setSecLevel17(int secLevel17)
    {
        this.secLevel17 = secLevel17;
    }

    public void setSecLevel18(int secLevel18)
    {
        this.secLevel18 = secLevel18;
    }

    public void setSecLevel19(int secLevel19)
    {
        this.secLevel19 = secLevel19;
    }

    public void setSecLevel20(int secLevel20)
    {
        this.secLevel20 = secLevel20;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    /**
     * オーナーアクセス権データ取得
     * 
     * @param hotelId ユーザ基本データ
     * @param hotelId ユーザ基本データ
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String hotelId, int userId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM owner_user_security WHERE hotelid = ? AND userid = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            prestate.setString( 1, hotelId );
            prestate.setInt( 2, userId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.hotelId = result.getString( "hotelid" );
                    this.userId = result.getInt( "userid" );
                    this.adminFlag = result.getInt( "admin_flag" );
                    this.secLevel01 = result.getInt( "sec_level01" );
                    this.secLevel02 = result.getInt( "sec_level02" );
                    this.secLevel03 = result.getInt( "sec_level03" );
                    this.secLevel04 = result.getInt( "sec_level04" );
                    this.secLevel05 = result.getInt( "sec_level05" );
                    this.secLevel06 = result.getInt( "sec_level06" );
                    this.secLevel07 = result.getInt( "sec_level07" );
                    this.secLevel08 = result.getInt( "sec_level08" );
                    this.secLevel09 = result.getInt( "sec_level09" );
                    this.secLevel10 = result.getInt( "sec_level10" );
                    this.secLevel11 = result.getInt( "sec_level11" );
                    this.secLevel12 = result.getInt( "sec_level12" );
                    this.secLevel13 = result.getInt( "sec_level13" );
                    this.secLevel14 = result.getInt( "sec_level14" );
                    this.secLevel15 = result.getInt( "sec_level15" );
                    this.secLevel16 = result.getInt( "sec_level16" );
                    this.secLevel17 = result.getInt( "sec_level17" );
                    this.secLevel18 = result.getInt( "sec_level18" );
                    this.secLevel19 = result.getInt( "sec_level19" );
                    this.secLevel20 = result.getInt( "sec_level20" );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOwnerUserSecurity.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(true);
    }

    /**
     * ユーザ基本データ設定
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
                this.hotelId = result.getString( "hotelid" );
                this.userId = result.getInt( "userid" );
                this.adminFlag = result.getInt( "admin_flag" );
                this.secLevel01 = result.getInt( "sec_level01" );
                this.secLevel02 = result.getInt( "sec_level02" );
                this.secLevel03 = result.getInt( "sec_level03" );
                this.secLevel04 = result.getInt( "sec_level04" );
                this.secLevel05 = result.getInt( "sec_level05" );
                this.secLevel06 = result.getInt( "sec_level06" );
                this.secLevel07 = result.getInt( "sec_level07" );
                this.secLevel08 = result.getInt( "sec_level08" );
                this.secLevel09 = result.getInt( "sec_level09" );
                this.secLevel10 = result.getInt( "sec_level10" );
                this.secLevel11 = result.getInt( "sec_level11" );
                this.secLevel12 = result.getInt( "sec_level12" );
                this.secLevel13 = result.getInt( "sec_level13" );
                this.secLevel14 = result.getInt( "sec_level14" );
                this.secLevel15 = result.getInt( "sec_level15" );
                this.secLevel16 = result.getInt( "sec_level16" );
                this.secLevel17 = result.getInt( "sec_level17" );
                this.secLevel18 = result.getInt( "sec_level18" );
                this.secLevel19 = result.getInt( "sec_level19" );
                this.secLevel20 = result.getInt( "sec_level20" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataOwnerUserSecurity.setData] Exception=" + e.toString() );
        }

        return(true);
    }

}
