package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Message;

/**
 * カードレスメンバー利用分析ビジネスロジック
 */
public class LogicOwnerBkoAnalysisCardless implements Serializable
{
    private static final long            serialVersionUID = 7755247690249534136L;
    String                               strTitle         = "カードレスメンバー利用分析";

    private FormOwnerBkoAnalysisCardless frm;

    /* フォームオブジェクト */
    public FormOwnerBkoAnalysisCardless getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoAnalysisCardless frm)
    {
        this.frm = frm;
    }

    /**
     * カードレスメンバーリスト取得
     * 
     * @param なし
     * @return なし
     */
    public void getAnalysisCardless() throws Exception
    {

        // ■カードレスメンバー新規件数・カードレスメンバー移行件数
        getTotal();

        // ■対象月の移行済みカード
        geDetail();

    }

    /**
     * カードレスメンバー新規件数・カードレスメンバー移行件数取得
     * 
     * @param なし
     * @return なし
     */
    private void getTotal() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int sumNewMember = 0;
        int sumIkouMember = 0;

        query = "SELECT count(CASE WHEN auto_flag=1 THEN 1 END) AS newMember, count(CASE WHEN auto_flag=0 THEN 1 END) AS ikouMember FROM ap_hotel_custom";
        query = query + " WHERE regist_date >= ? AND regist_date <= ?";
        query = query + "   AND id = ?";
        query = query + "   AND del_flag = 0";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getDateFrom() );
            prestate.setInt( 2, frm.getDateTo() );
            prestate.setInt( 3, frm.getSelHotelID() );
            result = prestate.executeQuery();
            if ( result.next() )
            {
                sumNewMember = result.getInt( "newMember" );
                sumIkouMember = result.getInt( "ikouMember" );
            }

            frm.setSumNewMember( sumNewMember );
            frm.setSumIkouMember( sumIkouMember );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerAnalysisCardress.getTotal] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }

    /**
     * 明細情報取得
     * 
     * @param int usageDate 利用日
     * @param int accTitleCd 科目コード
     *        (その他の場合、-1を設定する)
     * @param int selKbn (1:利用金額取得、2：予約金額取得、3:明細の金額, 4:件数)
     * @return int 金額、または件数
     */
    private void geDetail() throws Exception
    {
        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        int count = 0;
        int ret = 0;
        ArrayList<Integer> registDateList = new ArrayList<Integer>(); // 移行日
        ArrayList<String> customIdList = new ArrayList<String>(); // 移行済みカード
        ArrayList<Integer> userSeqList = new ArrayList<Integer>(); // ユーザーseq

        query = "SELECT ahc.custom_id AS customId, ahc.regist_date AS registDate, hudi.user_seq AS userSeq FROM ap_hotel_custom ahc";
        query = query + " LEFT JOIN hh_user_data_index hudi ";
        query = query + "  ON ahc.id= hudi.id  ";
        query = query + "  AND  ahc.user_id= hudi.user_id  ";
        query = query + " WHERE ahc.regist_date >= ? AND ahc.regist_date <= ?";
        query = query + "   AND ahc.id = ?";
        query = query + "   AND ahc.auto_flag = 0";
        query = query + "   AND ahc.del_flag = 0";
        query = query + "   ORDER BY ahc.regist_date,ahc.custom_id ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, frm.getDateFrom() );
            prestate.setInt( 2, frm.getDateTo() );
            prestate.setInt( 3, frm.getSelHotelID() );

            result = prestate.executeQuery();
            while( result.next() )
            {
                registDateList.add( result.getInt( "registDate" ) );
                customIdList.add( result.getString( "customId" ) );
                userSeqList.add( result.getInt( "userSeq" ) );
            }

            // レコード件数取得
            if ( result.last() != false )
            {
                count = result.getRow();
            }

            // 該当データがない場合
            if ( count == 0 )
            {
                frm.setErrMsg( Message.getMessage( "erro.30001", "移行済みカードレスメンバー" ) );
                return;
            }

            frm.setRegistDateList( registDateList );
            frm.setCustomIdList( customIdList );
            frm.setUserSeqList( userSeqList );

        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerAnalysisCardress.geDetail] Exception=" + e.toString() );
            throw new Exception( e );

        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

    }

}
