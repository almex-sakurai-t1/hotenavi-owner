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
 * �O���[�v�X�������W�b�N
 */
public class LogicOwnerBkoGroupBill implements Serializable
{

    /**
     *
     */
    private static final long     serialVersionUID = -5968841751198590811L;

    private FormOwnerBkoGroupBill frm;

    /* �t�H�[���I�u�W�F�N�g */
    public FormOwnerBkoGroupBill getFrm()
    {
        return frm;
    }

    public void setFrm(FormOwnerBkoGroupBill frm)
    {
        this.frm = frm;
    }

    /**
     * �������擾
     * 
     * @param �Ȃ�
     * @return �Ȃ�
     */
    public void getGroupBill() throws Exception
    {
        int sumIncome = 0;
        int sumOutgo = 0;
        ArrayList<Integer> idList = new ArrayList<Integer>(); // �z�e��ID
        ArrayList<String> hotenaviIdList = new ArrayList<String>(); // �z�e�i�rID
        ArrayList<String> hotelNameList = new ArrayList<String>(); // �z�e����
        ArrayList<String> billNameList = new ArrayList<String>(); // �@�l��
        ArrayList<Integer> incomeList = new ArrayList<Integer>(); // ����
        ArrayList<Integer> outgoList = new ArrayList<Integer>(); // �x�o

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
            // hh_bko_closing_control����Ώی��̏���ŗ����擾����
            tax = OwnerBkoCommon.getTax( billDate );

            // �O���[�v�z�e�����擾����
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

                // �����i�A�����b�N�X���Ō����Ƃ��͎x�������A�z�e�������猩���Ƃ��͎����j
                seisanPay = OwnerBkoCommon.getAccountRecvDetail( id, billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_120, 3 );
                seisanPay = seisanPay * -1;

                // ��ʂł́A�z�e�������猩���Ƃ��́A�x���A�������t�ƂȂ邽�߁A�{�A�|�̕������t�ɂ��ĕ\������
                // �x���i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
                seisanSeikyu = OwnerBkoCommon.getAccountRecvDetail( id, billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_110, 3 );
                seisanSeikyu = OwnerBkoCommon.reCalctTax( seisanSeikyu, tax );

                // �x���i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
                rsvSeikyu = OwnerBkoCommon.getAccountRecvDetail( id, billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_140, 3 );
                rsvSeikyu = OwnerBkoCommon.reCalctTax( rsvSeikyu, tax );

                rsvBonus = OwnerBkoCommon.getAccountRecvDetail( id, billDate, OwnerBkoCommon.ACCOUNT_TITLE_CD_150, 3 );
                rsvBonus = OwnerBkoCommon.reCalctTax( rsvBonus, tax );

                // �x���i�A�����b�N�X���Ō����Ƃ��͎��������A�z�e�������猩���Ƃ��͎x���j
                otherSeikyu = OwnerBkoCommon.getAccountRecvDetail( id, billDate, -1, 3 );
                otherSeikyu = OwnerBkoCommon.reCalctTax( otherSeikyu, tax );

                // �������x�i���� �z�e������݂���x�o�j
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
