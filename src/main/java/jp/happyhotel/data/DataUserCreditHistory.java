package jp.happyhotel.data;

/*
 * Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Mon Nov 07 11:44:53 JST 2011
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * DataUserCreditHistory.
 * 
 * @author tanabe-y2
 * @version 1.0
 *          history
 *          Symbol Date Person Note
 *          [1] 2011/11/07 tanabe-y2 Generated.
 */
public class DataUserCreditHistory implements Serializable
{

    public static final String TABLE                             = "HH_USER_CREDIT_HISTORY";

    // 不明
    public static final int    HISTORYTYPE_UNKNOWN               = 0;
    // 登録
    public static final int    HISTORYTYPE_REGIST                = 1;
    // 解約登録
    public static final int    HISTORYTYPE_UNREGIST              = 2;
    // 変更登録
    public static final int    HISTORYTYPE_CHANGE                = 3;
    // 通常退会
    public static final int    HISTORYTYPE_WITHDRAW              = 4;
    // 強制退会
    public static final int    HISTORYTYPE_FORCEWIDHDRAW         = 5;
    // 会員登録
    public static final int    HISTORYTYPE_GMO_MEMBERREGIST      = 10;
    // カード登録
    public static final int    HISTORYTYPE_GMO_CARDREGIST        = 11;
    // カード参照
    public static final int    HISTORYTYPE_GMO_CARDSEARCH        = 12;
    // カード削除
    public static final int    HISTORYTYPE_GMO_CARDDELETE        = 13;
    // 会員削除
    public static final int    HISTORYTYPE_GMO_MEMBERDELETE      = 14;
    // 会員参照
    public static final int    HISTORYTYPE_GMO_MEMBERSEARCH      = 15;
    // 会員ID逆引き
    public static final int    HISTORYTYPE_GMO_SEARCHCARDREVERSE = 16;

    // 共通エラーコード
    public static final String COMMON_ERRORCODE                  = "999";
    // 共通エラー詳細コード
    public static final String COMMON_ERRORDETAILCODE            = "999999999";

    /**
     * user_id:varchar(32) <Primary Key>
     */
    private String             user_id;

    /**
     * auto_increment:int(10) <Primary Key>
     */
    private int                seq_no;

    /**
     * card_seq_no:int(10)
     */
    private int                card_seq_no;

    /**
     * generate_date:int(10)
     */
    private int                generate_date;

    /**
     * generate_time:int(10)
     */
    private int                generate_time;

    /**
     * error_code:char(3)
     */
    private String             error_code;

    /**
     * error_detail_code:varchar(9)
     */
    private String             error_detail_code;

    /**
     * history_status:int(10)
     */
    private int                history_status;

    /**
     * データを初期化します。
     */
    public DataUserCreditHistory()
    {
        user_id = "";
        seq_no = 0;
        card_seq_no = 0;
        generate_date = 0;
        generate_time = 0;
        error_code = "";
        error_detail_code = "";
        history_status = 0;
    }

    public String getUser_id()
    {
        return this.user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public int getSeq_no()
    {
        return this.seq_no;
    }

    public void setSeq_no(int seq_no)
    {
        this.seq_no = seq_no;
    }

    public int getCard_seq_no()
    {
        return this.card_seq_no;
    }

    public void setCard_seq_no(int card_seq_no)
    {
        this.card_seq_no = card_seq_no;
    }

    public int getGenerate_date()
    {
        return this.generate_date;
    }

    public void setGenerate_date(int generate_date)
    {
        this.generate_date = generate_date;
    }

    public int getGenerate_time()
    {
        return this.generate_time;
    }

    public void setGenerate_time(int generate_time)
    {
        this.generate_time = generate_time;
    }

    public String getError_code()
    {
        return this.error_code;
    }

    public void setError_code(String error_code)
    {
        this.error_code = error_code;
    }

    public String getError_detail_code()
    {
        return this.error_detail_code;
    }

    public void setError_detail_code(String error_detail_code)
    {
        this.error_detail_code = error_detail_code;
    }

    public int getHistory_status()
    {
        return this.history_status;
    }

    public void setHistory_status(int history_status)
    {
        this.history_status = history_status;
    }

    /**
     * ユーザクレジット会員履歴追加
     * 
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

        query = "INSERT hh_user_credit_history SET ";
        query = query + " user_id = ?,";
        query = query + " card_seq_no = ?,";
        query = query + " generate_date = ?,";
        query = query + " generate_time = ?,";
        query = query + " error_code = ?,";
        query = query + " error_detail_code = ?,";
        query = query + " history_status = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( 1, this.user_id );
            prestate.setInt( 2, this.card_seq_no );
            prestate.setInt( 3, this.generate_date );
            prestate.setInt( 4, this.generate_time );
            prestate.setString( 5, this.error_code );
            prestate.setString( 6, this.error_detail_code );
            prestate.setInt( 7, this.history_status );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataUserCreditHistory.insertData] Exception=" + e.toString() );
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
