package jp.happyhotel.owner;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.OwnerBkoCommon;

/**
 * グループ店請求ロジック
 */
public class LogicOwnerBkoGroupBill implements Serializable
{

    /**
     *
     */
    private static final long     serialVersionUID = -5968841751198590811L;

    private FormOwnerBkoGroupBill frm;

    /* フォームオブジェクト */
    public FormOwnerBkoGroupBill getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoGroupBill frm)
    {
        this.frm = frm;
    }

    /**
     * 請求情報取得
     * 
     * @param なし
     * @return なし
     */
    public void getGroupBill() throws Exception
    {
        int sumIncome = 0;
        int sumOutgo = 0;
        ArrayList<Integer> idList = new ArrayList<Integer>(); // ホテルID
        ArrayList<String> hotenaviIdList = new ArrayList<String>(); // ホテナビID
        ArrayList<String> hotelNameList = new ArrayList<String>(); // ホテル名
        ArrayList<String> billNameList = new ArrayList<String>(); // 法人名
        ArrayList<Integer> incomeList = new ArrayList<Integer>(); // 収入
        ArrayList<Integer> outgoList = new ArrayList<Integer>(); // 支出

        String query = "";
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        int billDate = frm.getSelYear() * 100 + frm.getSelMonth();
        int tax = 0;
        int id = 0;
        int seisanSeikyu = 0;
        int rsvSeikyu = 0;
        int rsvBonus = 0;
        int otherSeikyu = 0;
        int seikyuIncome = 0;
        int seisanPay = 0;

        try
        {
            // hh_bko_closing_controlから対象月の消費税率を取得する
            tax = OwnerBkoCommon.getTax( billDate );

            // グループホテルを取得する
            query = " SELECT hbb.id AS Id" +
                    ",hhb.hotenavi_id AS hotenaviId" +
                    ",hhb.name AS hotelName" +
                    ",hbb.bill_name AS billName" +
                    // ",SUM(CASE WHEN hbbd.account_title_cd <> 120 THEN hbbd.amount ELSE 0 END) AS incomeAmount" +
                    // ",SUM(CASE WHEN hbbd.account_title_cd = 120 THEN -hbbd.amount ELSE 0 END) AS outgoAmount" +
                    " FROM hh_bko_bill AS hbb" +
                    // " INNER JOIN hh_bko_bill_detail AS hbbd ON hbbd.bill_slip_no = hbb.bill_slip_no" +
                    " INNER JOIN hh_hotel_basic AS hhb ON hhb.id = hbb.id" +
                    " INNER JOIN hh_bko_rel_billing_hotel AS hbrbh ON hbrbh.id = hbb.id" +
                    " INNER JOIN hh_bko_billing AS billing ON billing.bill_cd = hbrbh.bill_cd";
            if ( frm.getSelContractID().equals( "happyhotel" ) != false && frm.getSelHotelID() != 0 )
            {
                query += " INNER JOIN search_hotel_find AS shfd ON shfd.id = ?" +
                        " INNER JOIN search_hotel_find AS shfg ON (shfg.findstr7 = shfd.findstr7" +
                        " AND shfg.id = hbb.id" +
                        " AND shfg.findstr7 <> '')" +
                        " OR (shfg.id = hbb.id AND shfg.id = shfd.id)";
            }
            else
            {
                query += " INNER JOIN owner_user AS ou ON ou.hotelid = ? AND ou.userid = ?" +
                        " INNER JOIN owner_user_hotel AS ouh ON ouh.hotelid = ou.hotelid AND ouh.userid = ou.userid" +
                        " AND ouh.accept_hotelid = hhb.hotenavi_id";
            }
            query += " WHERE hbb.bill_date = ?" +
                    " GROUP BY hbb.id";

            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            if ( frm.getSelContractID().equals( "happyhotel" ) != false && frm.getSelHotelID() != 0 )
            {
                prestate.setInt( 1, frm.getSelHotelID() );
                prestate.setInt( 2, frm.getSelYear() * 100 + frm.getSelMonth() );
            }
            else
            {
                prestate.setString( 1, frm.getSelContractID() );
                prestate.setInt( 2, frm.getSelUserID() );
                prestate.setInt( 3, frm.getSelYear() * 100 + frm.getSelMonth() );
            }
            result = prestate.executeQuery();
            while( result.next() != false )
            {
                id = 0;
                seisanSeikyu = 0;
                rsvSeikyu = 0;
                rsvBonus = 0;
                otherSeikyu = 0;
                seikyuIncome = 0;
                seisanPay = 0;

                idList.add( result.getInt( "Id" ) );
                id = result.getInt( "Id" );

                // 収入（アルメックス側で見たときは支払だが、ホテル側から見たときは収入）
                seisanPay = OwnerBkoCommon.getAccountRecvDetail( id, billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, 3 );
                seisanPay = seisanPay * -1;

                // 画面では、ホテル側から見たときは、支払、収入が逆となるため、＋、－の符号を逆にして表示する
                // 支払（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
                seisanSeikyu = OwnerBkoCommon.getAccountRecvDetail( id, billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 3 );
                seisanSeikyu = OwnerBkoCommon.reCalctTax( seisanSeikyu, tax );

                // 支払（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
                rsvSeikyu = OwnerBkoCommon.getAccountRecvDetail( id, billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 3 );
                rsvSeikyu = OwnerBkoCommon.reCalctTax( rsvSeikyu, tax );

                rsvBonus = OwnerBkoCommon.getAccountRecvDetail( id, billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_150, 3 );
                rsvBonus = OwnerBkoCommon.reCalctTax( rsvBonus, tax );

                // 支払（アルメックス側で見たときは収入だが、ホテル側から見たときは支払）
                otherSeikyu = OwnerBkoCommon.getAccountRecvDetail( id, billDate, -1, 3 );
                otherSeikyu = OwnerBkoCommon.reCalctTax( otherSeikyu, tax );

                // ■ご収支（収入 ホテルからみたら支出）
                seikyuIncome = (seisanSeikyu + rsvSeikyu + rsvBonus + otherSeikyu);

                hotenaviIdList.add( result.getString( "hotenaviId" ) );
                hotelNameList.add( result.getString( "hotelName" ) );
                billNameList.add( result.getString( "billName" ) );
                incomeList.add( seikyuIncome );
                outgoList.add( seisanPay );
                sumIncome += seikyuIncome;
                sumOutgo += seisanPay;
            }
            frm.setIdList( idList );
            frm.setHotenaviIdList( hotenaviIdList );
            frm.setHotelNameList( hotelNameList );
            frm.setBillNameList( billNameList );
            frm.setIncomeList( incomeList );
            frm.setOutgoList( outgoList );
            frm.setSumIncome( sumIncome );
            frm.setSumOutgo( sumOutgo );
        }
        catch ( Exception e )
        {
            Logging.error( "[LogicOwnerBkoGroupBill.getGroupBill] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

    }

}
