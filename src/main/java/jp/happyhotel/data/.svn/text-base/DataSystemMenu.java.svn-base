package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Fri Jul 20 11:53:01 JST 2012
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;

/**
 * Hh_system_menuVo.
 * 
 * @author tashiro-s1
 * @version 1.0
 *          history
 *          Symbol Date Person Note
 *          [1] 2012/07/20 tashiro-s1 Generated.
 */
public class DataSystemMenu implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -7975602684113024812L;
    private int               kind;
    private int               seq;
    private int               startDate;
    private int               endDate;
    private int               dispNo;
    private String            text;
    private String            textUrl;
    private String            iconUrl;
    private String            bannerUrl;
    private int               delFlag;

    public DataSystemMenu()
    {
        kind = 0;
        seq = 0;
        startDate = 0;
        endDate = 0;
        text = "";
        textUrl = "";
        iconUrl = "";
        bannerUrl = "";
        delFlag = 0;
    }

    public int getKind()
    {
        return this.kind;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public int getSeq()
    {
        return this.seq;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public int getStartDate()
    {
        return this.startDate;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    public int getEndDate()
    {
        return this.endDate;
    }

    public void setEndDate(int endDate)
    {
        this.endDate = endDate;
    }

    public int getDispNo()
    {
        return this.dispNo;
    }

    public void setDispNo(int dispNo)
    {
        this.dispNo = dispNo;
    }

    public String getText()
    {
        return this.text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getTextUrl()
    {
        return this.textUrl;
    }

    public void setTextUrl(String textUrl)
    {
        this.textUrl = textUrl;
    }

    public String getIconUrl()
    {
        return Url.convertUrl( this.iconUrl );
    }

    public void setIconUrl(String iconUrl)
    {
        this.iconUrl = iconUrl;
    }

    public String getBannerUrl()
    {
        return Url.convertUrl( this.bannerUrl );
    }

    public void seBannerUrl(String bannerUrl)
    {
        this.bannerUrl = bannerUrl;
    }

    public int getDelFlag()
    {
        return this.delFlag;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    /****
     * メニューデータ取得
     * 
     * @param kind
     * @param seq
     * @return
     */
    public boolean getData(int kind, int seq)
    {

        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT * FROM hh_system_menu WHERE kind = ? AND seq = ?";

        count = 0;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, kind );
            prestate.setInt( 2, seq );
            result = prestate.executeQuery();

            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.kind = result.getInt( "kind" );
                    this.seq = result.getInt( "seq" );
                    this.startDate = result.getInt( "start_date" );
                    this.endDate = result.getInt( "end_date" );
                    this.dispNo = result.getInt( "disp_no" );
                    this.text = result.getString( "text" );
                    this.textUrl = result.getString( "text_url" );
                    this.iconUrl = result.getString( "icon_url" );
                    this.bannerUrl = result.getString( "banner_url" );
                    this.delFlag = result.getInt( "del_flag" );
                }

                if ( result.last() != false )
                {
                    count = result.getRow();
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMenu.getData] Exception=" + e.toString() );
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        if ( count > 0 )
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /**
     * フェリカ紐付けデータ設定
     * 
     * @param result ユーザポイントデータレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.kind = result.getInt( "kind" );
                this.seq = result.getInt( "seq" );
                this.startDate = result.getInt( "start_date" );
                this.endDate = result.getInt( "end_date" );
                this.dispNo = result.getInt( "disp_no" );
                this.text = result.getString( "text" );
                this.textUrl = result.getString( "text_url" );
                this.iconUrl = result.getString( "icon_url" );
                this.bannerUrl = result.getString( "banner_url" );
                this.delFlag = result.getInt( "del_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMenu.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * フェリカ紐付けデータ挿入
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

        query = "INSERT hh_system_felica_matching SET ";
        query += " kind = ?,";
        query += " seq = ?,";
        query += " start_date = ?,";
        query += " end_date = ?,";
        query += " text = ?,";
        query += " text_url = ?,";
        query += " icon_url = ?,";
        query += " banner_url = ?,";
        query += " del_flag = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.kind );
            prestate.setInt( 2, this.seq );
            prestate.setInt( 3, this.startDate );
            prestate.setInt( 4, this.endDate );
            prestate.setString( 5, this.text );
            prestate.setString( 6, this.textUrl );
            prestate.setString( 7, this.iconUrl );
            prestate.setString( 8, this.bannerUrl );
            prestate.setInt( 9, this.delFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMenu.insertData] Exception=" + e.toString() );
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
     * フェリカ紐付けデータ更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param key1　アクセスキー1
     * @param key2　アクセスキー2
     * @return
     */
    public boolean updateData(int kind, int seq)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_system_felica_matching SET ";
        query += " start_date = ?,";
        query += " end_date = ?,";
        query += " text = ?,";
        query += " text_url = ?,";
        query += " icon_url = ?,";
        query += " banner_url = ?,";
        query += " del_flag = ?";
        query = query + " WHERE kind = ? AND seq = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( 1, this.startDate );
            prestate.setInt( 2, this.endDate );
            prestate.setString( 3, this.text );
            prestate.setString( 4, this.textUrl );
            prestate.setString( 5, this.iconUrl );
            prestate.setString( 6, this.bannerUrl );
            prestate.setInt( 7, this.delFlag );
            prestate.setInt( 8, kind );
            prestate.setInt( 9, seq );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataSystemMenu.updateData] Exception=" + e.toString() );
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
