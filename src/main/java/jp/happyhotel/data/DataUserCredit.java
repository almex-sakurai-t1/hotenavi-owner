package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Mon Nov 07 10:09:27 JST 2011
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * DataUserCredit
 * 
 * @author tanabe-y2
 * @version 1.0
 *          history
 *          Symbol Date Person Note
 *          [1] 2011/11/07 tanabe-y2 Generated.
 */
public class DataUserCredit implements Serializable
{

    public static final String TABLE               = "HH_USER_CREDIT";

    // 非課金
    public static final int    CHARGEFLAG_FREE     = 0;
    // 課金
    public static final int    CHARGEFLAG_PAY      = 1;
    // 初回課金
    public static final int    CHARGEFLAG_FIRSTPAY = 2;
    // 課金NG会員
    public static final int    CHARGEFLAG_NGMEMBER = 3;
    // アルメックス会員(課金非対称)
    public static final int    CHARGEFLAG_ALMEX    = 99;
    // 退会フラグなし
    public static final int    DELETEFLAG_FALSE    = 0;
    // 退会フラグあり
    public static final int    DELETEFLAG_TRUE     = 1;

    /**
     * user_id:varchar(32) <Primary Key>
     */
    private String             user_id;

    /**
     * card_seq_no:int(10) <Primary Key>
     */
    private int                card_seq_no;

    /**
     * regist_date:int(10)
     */
    private int                regist_date;

    /**
     * regist_time:int(10)
     */
    private int                regist_time;

    /**
     * del_date:int(10)
     */
    private int                del_date;

    /**
     * del_time:int(10)
     */
    private int                del_time;

    /**
     * charge_flag:int(10)
     */
    private int                charge_flag;

    /**
     * del_flag:int(10)
     */
    private int                del_flag;

    /**
     * Constractor
     */
    public DataUserCredit()
    {
        user_id = "";
        card_seq_no = 0;
        regist_date = 0;
        regist_time = 0;
        del_date = 0;
        del_time = 0;
        charge_flag = 0;
        del_flag = 0;
    }

    public String getUser_id()
    {
        return this.user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public int getCard_seq_no()
    {
        return this.card_seq_no;
    }

    public void setCard_seq_no(int card_seq_no)
    {
        this.card_seq_no = card_seq_no;
    }

    public int getRegist_date()
    {
        return this.regist_date;
    }

    public void setRegist_date(int regist_date)
    {
        this.regist_date = regist_date;
    }

    public int getRegist_time()
    {
        return this.regist_time;
    }

    public void setRegist_time(int regist_time)
    {
        this.regist_time = regist_time;
    }

    public int getDel_date()
    {
        return this.del_date;
    }

    public void setDel_date(int del_date)
    {
        this.del_date = del_date;
    }

    public int getDel_time()
    {
        return this.del_time;
    }

    public void setDel_time(int del_time)
    {
        this.del_time = del_time;
    }

    public int getCharge_flag()
    {
        return this.charge_flag;
    }

    public void setCharge_flag(int charge_flag)
    {
        this.charge_flag = charge_flag;
    }

    public int getDel_flag()
    {
        return this.del_flag;
    }

    public void setDel_flag(int del_flag)
    {
        this.del_flag = del_flag;
    }

    /**
     * ユーザクレジットデータ取得
     * 
     * @param userId ユーザ基本データ
     * @param card_seqNo カード登録連番
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String userId, int card_seqNo)
    {
        boolean ret = false;

        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_user_credit WHERE user_id = ? AND card_seq_no = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, userId );
            prestate.setInt( 2, card_seqNo );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.user_id = result.getString( "user_id" );
                    this.card_seq_no = result.getInt( "card_seq_no" );
                    this.regist_date = result.getInt( "regist_date" );
                    this.regist_time = result.getInt( "regist_time" );
                    this.del_date = result.getInt( "del_date" );
                    this.del_time = result.getInt( "del_time" );
                    this.charge_flag = result.getInt( "charge_flag" );
                    this.del_flag = result.getInt( "del_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserCredit.getData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ユーザクレジットデータ設定
     * 
     * @param result ユーザクレジットデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret = false;
        try
        {
            if ( result != null )
            {
                this.user_id = result.getString( "user_id" );
                this.card_seq_no = result.getInt( "card_seq_no" );
                this.regist_date = result.getInt( "regist_date" );
                this.regist_time = result.getInt( "regist_time" );
                this.del_date = result.getInt( "del_date" );
                this.del_time = result.getInt( "del_time" );
                this.charge_flag = result.getInt( "charge_flag" );
                this.del_flag = result.getInt( "del_flag" );
            }
            ret = true;
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserCredit.setData] Exception=" + e.toString() );
            ret = false;
        }

        return(ret);
    }

    /**
     * ユーザクレジットデータ追加
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        int result;
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        query = "INSERT hh_user_credit SET ";
        query = query + " user_id = ?,";
        query = query + " card_seq_no = ?,";
        query = query + " regist_date = ?,";
        query = query + " regist_time = ?,";
        query = query + " del_date = ?,";
        query = query + " del_time = ?,";
        query = query + " charge_flag = ?,";
        query = query + " del_flag = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.user_id );
            prestate.setInt( 2, this.card_seq_no );
            prestate.setInt( 3, this.regist_date );
            prestate.setInt( 4, this.regist_time );
            prestate.setInt( 5, this.del_date );
            prestate.setInt( 6, this.del_time );
            prestate.setInt( 7, this.charge_flag );
            prestate.setInt( 8, this.del_flag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserCredit.insertData] Exception=" + e.toString() );
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
     * ユーザクレジットデータ更新
     * 
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData()
    {
        int result;
        boolean ret = false;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        try
        {
            if ( !user_id.equals( "" ) )
            {
                query = "UPDATE hh_user_credit SET ";
                query = query + " regist_date = ?,";
                query = query + " regist_time = ?,";
                query = query + " del_date = ?,";
                query = query + " del_time = ?,";
                query = query + " charge_flag = ?,";
                query = query + " del_flag = ?";
                query = query + " WHERE user_id = ?";
                query = query + " AND card_seq_no = ?";

                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                // 更新対象の値をセットする
                prestate.setInt( 1, this.regist_date );
                prestate.setInt( 2, this.regist_time );
                prestate.setInt( 3, this.del_date );
                prestate.setInt( 4, this.del_time );
                prestate.setInt( 5, this.charge_flag );
                prestate.setInt( 6, this.del_flag );
                prestate.setString( 7, this.user_id );
                prestate.setInt( 8, this.card_seq_no );

                result = prestate.executeUpdate();
                if ( result > 0 )
                {
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserCredit.updateData] Exception=" + e.toString() );
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
