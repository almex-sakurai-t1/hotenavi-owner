package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * ホテル別設定ファイル取得クラス
 * 
 * @author sakurai-t1
 * @version 1.00 2017/5/9
 */
public class DataHotelElement implements Serializable
{
    /**
     * 
     */
    private static final long  serialVersionUID = -4312153612808848009L;
    public static final String TABLE            = "hotel_element";
    private String             hotelId;                                 //
    private int                chkAgeFlg;                               // 年齢確認フラグ （1:クッションページあり）
    private String             htmlHead;                                // HTMLヘッダ情報
    private String             htmlLoginForm;                           // ログインフォーム
    private String             offlineflg;                              // ログイン有無フラグ（0:メンバーページなし 1:メンバーページあり）
    private String             mailmagazineflg;                         // メルマガ有無フラグ（0：無し 1:有）
    private String             mailtoflg;                               // ホテルへ一言有無フラグ（0：無し 1:有）
    private String             mailnameflg;                             // ホテルへ一言ホテル件名有無（0：件名にホテル名無し(default) 1:件名にホテル名有）
    private String             mailname;                                // ホテルへ一言ホテル名
    private String             viewflg;                                 // 0:通常　1:参照バージョン 2:参照バージョン（メンバーも） 9:休止中
    private String             bbsgroupflg;                             // 多店舗掲示板の使用(0:通常　1:多店舗バージョン)
    private String             prizeHotelid;                            // 商品交換用ホテルID（未入力の場合はhotel_idを使用）
    private int                couponMapFlg;                            // 0:Yahoo!MAPを使用,1:画像ファイルを使用
    private String             couponMapImg1;                           // クーポン画像1
    private String             couponMapImg2;                           // クーポン画像2
    private int                bbsTempFlg;                              // 0:通常掲示板,1:仮投稿掲示板（掲示板追加時にdel_flagに1をセット）
    private int                rankingHiddenFlg;                        // 0:通常,1:ランキング情報を出力しない
    private int                ownercount;                              // オーナーズルーム達成回数
    private int                trialDate;                               // リニューアル開始日付(YYYYMMDD)　
    private int                startDate;                               // 稼動日付(YYYYMMDD)　

    /**
     * データを初期化します。
     */
    public DataHotelElement()
    {
        this.hotelId = "";
        this.chkAgeFlg = 0;
        this.htmlHead = "";
        this.htmlLoginForm = "";
        this.offlineflg = "";
        this.mailmagazineflg = "";
        this.mailtoflg = "";
        this.mailnameflg = "";
        this.mailname = "";
        this.viewflg = "";
        this.bbsgroupflg = "";
        this.prizeHotelid = "";
        this.couponMapFlg = 0;
        this.couponMapImg1 = "";
        this.couponMapImg2 = "";
        this.bbsTempFlg = 0;
        this.rankingHiddenFlg = 0;
        this.ownercount = 0;
        this.trialDate = 0;
        this.startDate = 0;
    }

    public String getHotelId()
    {
        return hotelId;
    }

    public int getChkAgeFlg()
    {
        return chkAgeFlg;
    }

    public String getHtmlHead()
    {
        return htmlHead;
    }

    public String getHtmlLoginForm()
    {
        return htmlLoginForm;
    }

    public String getOfflineflg()
    {
        return offlineflg;
    }

    public String getMailmagazineflg()
    {
        return mailmagazineflg;
    }

    public String getMailtoflg()
    {
        return mailtoflg;
    }

    public String getMailnameflg()
    {
        return mailnameflg;
    }

    public String getMailname()
    {
        return mailname;
    }

    public String getViewflg()
    {
        return viewflg;
    }

    public String getBbsgroupflg()
    {
        return bbsgroupflg;
    }

    public String getPrizeHotelid()
    {
        return prizeHotelid;
    }

    public int getCouponMapFlg()
    {
        return couponMapFlg;
    }

    public String getCouponMapImg1()
    {
        return couponMapImg1;
    }

    public String getCouponMapImg2()
    {
        return couponMapImg2;
    }

    public int getBbsTempFlg()
    {
        return bbsTempFlg;
    }

    public int getRankingHiddenFlg()
    {
        return rankingHiddenFlg;
    }

    public int getOwnercount()
    {
        return ownercount;
    }

    public int getTrialDate()
    {
        return trialDate;
    }

    public int getStartDate()
    {
        return startDate;
    }

    public void setHotelId(String hotelId)
    {
        this.hotelId = hotelId;
    }

    public void setChkAgeFlg(int chkAgeFlg)
    {
        this.chkAgeFlg = chkAgeFlg;
    }

    public void setHtmlHead(String htmlHead)
    {
        this.htmlHead = htmlHead;
    }

    public void setHtmlLoginForm(String htmlLoginForm)
    {
        this.htmlLoginForm = htmlLoginForm;
    }

    public void setOfflineflg(String offlineflg)
    {
        this.offlineflg = offlineflg;
    }

    public void setMailmagazineflg(String mailmagazineflg)
    {
        this.mailmagazineflg = mailmagazineflg;
    }

    public void setMailtoflg(String mailtoflg)
    {
        this.mailtoflg = mailtoflg;
    }

    public void setMailnameflg(String mailnameflg)
    {
        this.mailnameflg = mailnameflg;
    }

    public void setMailname(String mailname)
    {
        this.mailname = mailname;
    }

    public void setViewflg(String viewflg)
    {
        this.viewflg = viewflg;
    }

    public void setBbsgroupflg(String bbsgroupflg)
    {
        this.bbsgroupflg = bbsgroupflg;
    }

    public void setPrizeHotelid(String prizeHotelid)
    {
        this.prizeHotelid = prizeHotelid;
    }

    public void setCouponMapFlg(int couponMapFlg)
    {
        this.couponMapFlg = couponMapFlg;
    }

    public void setCouponMapImg1(String couponMapImg1)
    {
        this.couponMapImg1 = couponMapImg1;
    }

    public void setCouponMapImg2(String couponMapImg2)
    {
        this.couponMapImg2 = couponMapImg2;
    }

    public void setBbsTempFlg(int bbsTempFlg)
    {
        this.bbsTempFlg = bbsTempFlg;
    }

    public void setRankingHiddenFlg(int rankingHiddenFlg)
    {
        this.rankingHiddenFlg = rankingHiddenFlg;
    }

    public void setOwnercount(int ownercount)
    {
        this.ownercount = ownercount;
    }

    public void setTrialDate(int trialDate)
    {
        this.trialDate = trialDate;
    }

    public void setStartDate(int startDate)
    {
        this.startDate = startDate;
    }

    /****
     * ホテル別設定ファイル取得
     * 
     * @param hotelId
     * @return
     */
    public boolean getData(String hotelId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM hotenavi.hotel_element WHERE hotel_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, hotelId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    ret = setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelElement.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * ホテル別設定ファイル設定
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
                this.hotelId = result.getString( "hotel_id" );
                this.chkAgeFlg = result.getInt( "chk_age_flg" );
                this.htmlHead = result.getString( "html_head" );
                this.htmlLoginForm = result.getString( "html_login_form" );
                this.offlineflg = result.getString( "offlineflg" );
                this.mailmagazineflg = result.getString( "mailmagazineflg" );
                this.mailtoflg = result.getString( "mailtoflg" );
                this.mailnameflg = result.getString( "mailnameflg" );
                this.mailname = result.getString( "mailname" );
                this.viewflg = result.getString( "viewflg" );
                this.bbsgroupflg = result.getString( "bbsgroupflg" );
                this.prizeHotelid = result.getString( "prize_hotelid" );
                this.couponMapFlg = result.getInt( "coupon_map_flg" );
                this.couponMapImg1 = result.getString( "coupon_map_img1" );
                this.couponMapImg2 = result.getString( "coupon_map_img2" );
                this.bbsTempFlg = result.getInt( "bbs_temp_flg" );
                this.rankingHiddenFlg = result.getInt( "ranking_hidden_flg" );
                this.ownercount = result.getInt( "ownercount" );
                this.trialDate = result.getInt( "trial_date" );
                this.startDate = result.getInt( "start_date" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelElement.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * ホテル別設定ファイル挿入
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

        query = "INSERT hotenavi.hotel_element SET ";
        query += " hotel_id=?";
        query += ", chk_age_flg=?";
        query += ", html_head=?";
        query += ", html_login_form=?";
        query += ", offlineflg=?";
        query += ", mailmagazineflg=?";
        query += ", mailtoflg=?";
        query += ", mailnameflg=?";
        query += ", mailname=?";
        query += ", viewflg=?";
        query += ", bbsgroupflg=?";
        query += ", prize_hotelid=?";
        query += ", coupon_map_flg=?";
        query += ", coupon_map_img1=?";
        query += ", coupon_map_img2=?";
        query += ", bbs_temp_flg=?";
        query += ", ranking_hidden_flg=?";
        query += ", ownercount=?";
        query += ", trial_date=?";
        query += ", start_date=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setString( i++, this.hotelId );
            prestate.setInt( i++, this.chkAgeFlg );
            prestate.setString( i++, this.htmlHead );
            prestate.setString( i++, this.htmlLoginForm );
            prestate.setString( i++, this.offlineflg );
            prestate.setString( i++, this.mailmagazineflg );
            prestate.setString( i++, this.mailtoflg );
            prestate.setString( i++, this.mailnameflg );
            prestate.setString( i++, this.mailname );
            prestate.setString( i++, this.viewflg );
            prestate.setString( i++, this.bbsgroupflg );
            prestate.setString( i++, this.prizeHotelid );
            prestate.setInt( i++, this.couponMapFlg );
            prestate.setString( i++, this.couponMapImg1 );
            prestate.setString( i++, this.couponMapImg2 );
            prestate.setInt( i++, this.bbsTempFlg );
            prestate.setInt( i++, this.rankingHiddenFlg );
            prestate.setInt( i++, this.ownercount );
            prestate.setInt( i++, this.trialDate );
            prestate.setInt( i++, this.startDate );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelElement.insertData] Exception=" + e.toString() );
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
     * ホテル別設定ファイル更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param hotelId
     * @return
     */
    public boolean updateData(String hotelId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE hotenavi.hotel_element SET ";
        query += " chk_age_flg=?";
        query += ", html_head=?";
        query += ", html_login_form=?";
        query += ", offlineflg=?";
        query += ", mailmagazineflg=?";
        query += ", mailtoflg=?";
        query += ", mailnameflg=?";
        query += ", mailname=?";
        query += ", viewflg=?";
        query += ", bbsgroupflg=?";
        query += ", prize_hotelid=?";
        query += ", coupon_map_flg=?";
        query += ", coupon_map_img1=?";
        query += ", coupon_map_img2=?";
        query += ", bbs_temp_flg=?";
        query += ", ranking_hidden_flg=?";
        query += ", ownercount=?";
        query += ", trial_date=?";
        query += ", start_date=?";
        query += " WHERE hotel_id=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setInt( i++, this.chkAgeFlg );
            prestate.setString( i++, this.htmlHead );
            prestate.setString( i++, this.htmlLoginForm );
            prestate.setString( i++, this.offlineflg );
            prestate.setString( i++, this.mailmagazineflg );
            prestate.setString( i++, this.mailtoflg );
            prestate.setString( i++, this.mailnameflg );
            prestate.setString( i++, this.mailname );
            prestate.setString( i++, this.viewflg );
            prestate.setString( i++, this.bbsgroupflg );
            prestate.setString( i++, this.prizeHotelid );
            prestate.setInt( i++, this.couponMapFlg );
            prestate.setString( i++, this.couponMapImg1 );
            prestate.setString( i++, this.couponMapImg2 );
            prestate.setInt( i++, this.bbsTempFlg );
            prestate.setInt( i++, this.rankingHiddenFlg );
            prestate.setInt( i++, this.ownercount );
            prestate.setInt( i++, this.trialDate );
            prestate.setInt( i++, this.startDate );
            prestate.setString( i++, this.hotelId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHotelElement.updateData] Exception=" + e.toString() );
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
