package jp.happyhotel.others;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;

import org.apache.commons.lang.StringUtils;

/**
 * Yahoo!�A�g�p���i�z�e�����j�񋟃N���X�B
 * 
 * @author koshiba-y1
 */
public class YahooInformationProvider1 extends AbstractInformationProvider
{
    /** ��g���i�[�p�I�u�W�F�N�g */
    List<Map<String, Object>> data;

    /**
     * �R���X�g���N�^�B
     */
    public YahooInformationProvider1()
    {
        this.data = null;
    }

    /**
     * �f�[�^���o�B
     */
    @Override
    public void select()
    {
        List<Map<String, Object>> rows;
        try
        {
            rows = selectHotelData();
        }
        catch ( Exception e )
        {
            Logging.warn( "Exception e=" + e.toString() );
            throw new UnsupportedOperationException( e );
        }

        rows = irregularCompliant( rows );

        this.data = rows;
    }

    /**
     * �f�[�^�o�́B<br>
     * <br>
     * �f�[�^��CSV�`���̕�����ɕϊ����܂��B<br>
     * 
     * @return �O���񋟏��
     */
    @Override
    public String export()
    {
        if ( this.data == null )
        {
            throw new UnsupportedOperationException( "��� select() �Ńf�[�^�̒��o���s���K�v������܂��B" );
        }

        Serializer serializer = new CsvSerializer();
        return serializer.serialize( this.data );
    }

    /**
     * �z�e�����̎擾�B
     * 
     * @return ��g�p�̏��
     */
    private static List<Map<String, Object>> selectHotelData() throws Exception
    {
        final String happyhotelUrl = Url.getUrl();
        if ( StringUtils.isEmpty( happyhotelUrl ) )
            throw new UnsupportedOperationException( "�v���p�e�B�t�@�C������n�b�s�[�z�e����URL���擾�ł��܂���ł����B" );

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

        final String query = ""
                + "SELECT "
                + "    hh_hotel_basic.id AS `id`, "
                + "    CONCAT( "
                + "        '" + happyhotelUrl + "/detail/detail_top.jsp?id=', "
                + "        hh_hotel_basic.id "
                + "    ) AS `�n�b�s�[�E�z�e��URL`, "
                + "    hh_hotel_basic.name AS `�z�e����`, "
                + "    hh_hotel_basic.address_all AS `�Z��`, "
                + "    hh_hotel_basic.tel1 AS `�d�b�ԍ�1`, "
                + "    hh_hotel_basic.tel2 AS `�d�b�ԍ�2`, "
                + "    CASE "
                + "        WHEN CHARACTER_LENGTH(hh_hotel_basic.pr_detail) < 3 THEN "
                + "            hh_hotel_basic.pr "
                + "        ELSE "
                + "            hh_hotel_basic.pr_detail "
                + "    END AS `�z�e��PR`, "
                + "    hh_hotel_basic.access AS `�A�N�Z�X`, "
                + "    hh_hotel_basic.room_count AS `������`, "
                + "    CONCAT_WS( "
                + "        '�E', "
                + "        CASE WHEN hh_hotel_basic.type_building = 1 THEN '�r��' ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.type_kodate   = 1 THEN '�ˌ�' ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.type_rentou   = 1 THEN '�A��' ELSE NULL END, "
                + "        NULLIF(CONVERT(hh_hotel_basic.type_etc USING utf8), '') "
                + "    ) AS `�����`��`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.parking = 1 THEN "
                + "            '�L' "
                + "        WHEN hh_hotel_basic.parking = 9 THEN "
                + "            '��' "
                + "        ELSE "
                + "            '' "
                + "    END AS `���ԏ�`, "
                + "    hh_hotel_basic.parking_count AS `���ԏ�䐔`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.high_roof = 1 THEN "
                + "            CONCAT( "
                + "                '��', "
                + "                CASE "
                + "                    WHEN hh_hotel_basic.high_roof_count = 0 THEN "
                + "                        '' "
                + "                    ELSE "
                + "                        CONCAT('�i', hh_hotel_basic.high_roof_count, '��j') "
                + "                END "
                + "            ) "
                + "        WHEN hh_hotel_basic.high_roof = 9 THEN "
                + "            '�s��' "
                + "         ELSE "
                + "            '' "
                + "    END AS `�n�C���[�t`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.benefit = 1 THEN "
                + "            '�L' "
                + "        WHEN hh_hotel_basic.benefit = 9 THEN "
                + "            '��' "
                + "        ELSE "
                + "            '' "
                + "    END AS `�����o�[���T`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.roomservice = 1 THEN "
                + "            '�L' "
                + "        WHEN hh_hotel_basic.roomservice = 9 THEN "
                + "            '��' "
                + "        ELSE "
                + "            '' "
                + "    END AS `���[���T�[�r�X`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.credit = 1 THEN "
                + "            '��' "
                + "        WHEN hh_hotel_basic.credit = 9 THEN "
                + "            '�s��' "
                + "        ELSE "
                + "            '' "
                + "    END AS `�N���W�b�g`, "
                + "    CONCAT_WS( "
                + "        ' ', "
                + "        CASE WHEN hh_hotel_basic.credit_visa   = 1 THEN 'VISA'   ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.credit_master = 1 THEN 'MASTER' ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.credit_jcb    = 1 THEN 'JCB'    ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.credit_dc     = 1 THEN 'DC'     ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.credit_nicos  = 1 THEN 'NICOS'  ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.credit_amex   = 1 THEN 'AMEX'   ELSE NULL END, "
                + "        NULLIF(CONVERT(hh_hotel_basic.credit_etc USING utf8), '') "
                + "    ) AS `�N���W�b�g���`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.halfway = 1 THEN "
                + "            '��' "
                + "        WHEN hh_hotel_basic.halfway = 9 THEN "
                + "            '�s��' "
                + "        ELSE "
                + "            '' "
                + "    END AS `�r���O�o`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.coupon = 1 THEN "
                + "            '�L' "
                + "        WHEN hh_hotel_basic.coupon = 9 THEN "
                + "            '��' "
                + "        ELSE "
                + "            '' "
                + "    END AS `�N�[�|��`, "
                + "    CONCAT_WS( "
                + "        '�E', "
                + "        CASE "
                + "            WHEN hh_hotel_basic.possible_one = 9 AND hh_hotel_basic.possible_three = 9 THEN "
                + "                '2�l�̂�' "
                + "            ELSE "
                + "                NULL "
                + "        END, "
                + "        CASE WHEN hh_hotel_basic.possible_three = 1 THEN '3�l�i����2�l�j' ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.possible_one   = 1 THEN '1�l�̂݉�'       ELSE NULL END "
                + "    ) AS `���p�l��`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.reserve = 1 THEN "
                + "            '��' "
                + "        WHEN hh_hotel_basic.reserve = 9 THEN "
                + "            '�s��' "
                + "        ELSE "
                + "            '' "
                + "    END AS `�\��`, "
                + "    hh_hotel_basic.hotel_lat AS `�ܓx`, "
                + "    hh_hotel_basic.hotel_lon AS `�o�x`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.rank >= 2 THEN "
                + "            CONCAT('" + happyhotelUrl + "/servlet/HotelPicture?id=', hh_hotel_basic.id) "
                + "        ELSE "
                + "            '' "
                + "    END AS `�O�ρi���S�j�摜` "
                + "FROM "
                + "    hh_hotel_basic "
                + "WHERE "
                + "    99999 < hh_hotel_basic.id AND hh_hotel_basic.id < 89999999 "
                + "    AND hh_hotel_basic.rank >= 1 "
                + "    AND hh_hotel_basic.kind <= 7 "
                + "ORDER BY "
                + "    hh_hotel_basic.jis_code;";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet retSet = null;
        try
        {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement( query );
            retSet = stmt.executeQuery();

            if ( retSet != null )
            {
                while( retSet.next() )
                {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();

                    row.put( "id", retSet.getInt( "id" ) );
                    row.put( "�n�b�s�[�E�z�e��URL", retSet.getString( "�n�b�s�[�E�z�e��URL" ) );
                    row.put( "�z�e����", retSet.getString( "�z�e����" ) );
                    row.put( "�Z��", retSet.getString( "�Z��" ) );
                    row.put( "�d�b�ԍ�1", retSet.getString( "�d�b�ԍ�1" ) );
                    row.put( "�d�b�ԍ�2", retSet.getString( "�d�b�ԍ�2" ) );
                    row.put( "�z�e��PR", retSet.getString( "�z�e��PR" ) );
                    row.put( "�A�N�Z�X", retSet.getString( "�A�N�Z�X" ) );
                    row.put( "������", retSet.getInt( "������" ) );
                    row.put( "�����`��", retSet.getString( "�����`��" ) );
                    row.put( "���ԏ�", retSet.getString( "���ԏ�" ) );
                    row.put( "���ԏ�䐔", retSet.getInt( "���ԏ�䐔" ) );
                    row.put( "�n�C���[�t", retSet.getString( "�n�C���[�t" ) );
                    row.put( "�����o�[���T", retSet.getString( "�����o�[���T" ) );
                    row.put( "���[���T�[�r�X", retSet.getString( "���[���T�[�r�X" ) );
                    row.put( "�N���W�b�g", retSet.getString( "�N���W�b�g" ) );
                    row.put( "�N���W�b�g���", retSet.getString( "�N���W�b�g���" ) );
                    row.put( "�r���O�o", retSet.getString( "�r���O�o" ) );
                    row.put( "�N�[�|��", retSet.getString( "�N�[�|��" ) );
                    row.put( "���p�l��", retSet.getString( "���p�l��" ) );
                    row.put( "�\��", retSet.getString( "�\��" ) );
                    row.put( "�ܓx", retSet.getString( "�ܓx" ) );
                    row.put( "�o�x", retSet.getString( "�o�x" ) );
                    row.put( "�O�ρi���S�j�摜", retSet.getString( "�O�ρi���S�j�摜" ) );

                    rows.add( row );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.warn( "Exception e=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( retSet, stmt, conn );
        }

        return rows;
    }
}