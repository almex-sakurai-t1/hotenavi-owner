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
 * Yahoo!連携用情報（ホテル情報）提供クラス。
 * 
 * @author koshiba-y1
 */
public class YahooInformationProvider1 extends AbstractInformationProvider
{
    /** 提携情報格納用オブジェクト */
    List<Map<String, Object>> data;

    /**
     * コンストラクタ。
     */
    public YahooInformationProvider1()
    {
        this.data = null;
    }

    /**
     * データ抽出。
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
     * データ出力。<br>
     * <br>
     * データをCSV形式の文字列に変換します。<br>
     * 
     * @return 外部提供情報
     */
    @Override
    public String export()
    {
        if ( this.data == null )
        {
            throw new UnsupportedOperationException( "先に select() でデータの抽出を行う必要があります。" );
        }

        Serializer serializer = new CsvSerializer();
        return serializer.serialize( this.data );
    }

    /**
     * ホテル情報の取得。
     * 
     * @return 提携用の情報
     */
    private static List<Map<String, Object>> selectHotelData() throws Exception
    {
        final String happyhotelUrl = Url.getUrl();
        if ( StringUtils.isEmpty( happyhotelUrl ) )
            throw new UnsupportedOperationException( "プロパティファイルからハッピーホテルのURLを取得できませんでした。" );

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

        final String query = ""
                + "SELECT "
                + "    hh_hotel_basic.id AS `id`, "
                + "    CONCAT( "
                + "        '" + happyhotelUrl + "/detail/detail_top.jsp?id=', "
                + "        hh_hotel_basic.id "
                + "    ) AS `ハッピー・ホテルURL`, "
                + "    hh_hotel_basic.name AS `ホテル名`, "
                + "    hh_hotel_basic.address_all AS `住所`, "
                + "    hh_hotel_basic.tel1 AS `電話番号1`, "
                + "    hh_hotel_basic.tel2 AS `電話番号2`, "
                + "    CASE "
                + "        WHEN CHARACTER_LENGTH(hh_hotel_basic.pr_detail) < 3 THEN "
                + "            hh_hotel_basic.pr "
                + "        ELSE "
                + "            hh_hotel_basic.pr_detail "
                + "    END AS `ホテルPR`, "
                + "    hh_hotel_basic.access AS `アクセス`, "
                + "    hh_hotel_basic.room_count AS `部屋数`, "
                + "    CONCAT_WS( "
                + "        '・', "
                + "        CASE WHEN hh_hotel_basic.type_building = 1 THEN 'ビル' ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.type_kodate   = 1 THEN '戸建' ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.type_rentou   = 1 THEN '連棟' ELSE NULL END, "
                + "        NULLIF(CONVERT(hh_hotel_basic.type_etc USING utf8), '') "
                + "    ) AS `建物形式`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.parking = 1 THEN "
                + "            '有' "
                + "        WHEN hh_hotel_basic.parking = 9 THEN "
                + "            '無' "
                + "        ELSE "
                + "            '' "
                + "    END AS `駐車場`, "
                + "    hh_hotel_basic.parking_count AS `駐車場台数`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.high_roof = 1 THEN "
                + "            CONCAT( "
                + "                '可', "
                + "                CASE "
                + "                    WHEN hh_hotel_basic.high_roof_count = 0 THEN "
                + "                        '' "
                + "                    ELSE "
                + "                        CONCAT('（', hh_hotel_basic.high_roof_count, '台）') "
                + "                END "
                + "            ) "
                + "        WHEN hh_hotel_basic.high_roof = 9 THEN "
                + "            '不可' "
                + "         ELSE "
                + "            '' "
                + "    END AS `ハイルーフ`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.benefit = 1 THEN "
                + "            '有' "
                + "        WHEN hh_hotel_basic.benefit = 9 THEN "
                + "            '無' "
                + "        ELSE "
                + "            '' "
                + "    END AS `メンバー特典`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.roomservice = 1 THEN "
                + "            '有' "
                + "        WHEN hh_hotel_basic.roomservice = 9 THEN "
                + "            '無' "
                + "        ELSE "
                + "            '' "
                + "    END AS `ルームサービス`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.credit = 1 THEN "
                + "            '可' "
                + "        WHEN hh_hotel_basic.credit = 9 THEN "
                + "            '不可' "
                + "        ELSE "
                + "            '' "
                + "    END AS `クレジット`, "
                + "    CONCAT_WS( "
                + "        ' ', "
                + "        CASE WHEN hh_hotel_basic.credit_visa   = 1 THEN 'VISA'   ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.credit_master = 1 THEN 'MASTER' ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.credit_jcb    = 1 THEN 'JCB'    ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.credit_dc     = 1 THEN 'DC'     ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.credit_nicos  = 1 THEN 'NICOS'  ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.credit_amex   = 1 THEN 'AMEX'   ELSE NULL END, "
                + "        NULLIF(CONVERT(hh_hotel_basic.credit_etc USING utf8), '') "
                + "    ) AS `クレジット種類`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.halfway = 1 THEN "
                + "            '可' "
                + "        WHEN hh_hotel_basic.halfway = 9 THEN "
                + "            '不可' "
                + "        ELSE "
                + "            '' "
                + "    END AS `途中外出`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.coupon = 1 THEN "
                + "            '有' "
                + "        WHEN hh_hotel_basic.coupon = 9 THEN "
                + "            '無' "
                + "        ELSE "
                + "            '' "
                + "    END AS `クーポン`, "
                + "    CONCAT_WS( "
                + "        '・', "
                + "        CASE "
                + "            WHEN hh_hotel_basic.possible_one = 9 AND hh_hotel_basic.possible_three = 9 THEN "
                + "                '2人のみ' "
                + "            ELSE "
                + "                NULL "
                + "        END, "
                + "        CASE WHEN hh_hotel_basic.possible_three = 1 THEN '3人可（女性2人）' ELSE NULL END, "
                + "        CASE WHEN hh_hotel_basic.possible_one   = 1 THEN '1人のみ可'       ELSE NULL END "
                + "    ) AS `利用人数`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.reserve = 1 THEN "
                + "            '可' "
                + "        WHEN hh_hotel_basic.reserve = 9 THEN "
                + "            '不可' "
                + "        ELSE "
                + "            '' "
                + "    END AS `予約`, "
                + "    hh_hotel_basic.hotel_lat AS `緯度`, "
                + "    hh_hotel_basic.hotel_lon AS `経度`, "
                + "    CASE "
                + "        WHEN hh_hotel_basic.rank >= 2 THEN "
                + "            CONCAT('" + happyhotelUrl + "/servlet/HotelPicture?id=', hh_hotel_basic.id) "
                + "        ELSE "
                + "            '' "
                + "    END AS `外観（ロゴ）画像` "
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
                    row.put( "ハッピー・ホテルURL", retSet.getString( "ハッピー・ホテルURL" ) );
                    row.put( "ホテル名", retSet.getString( "ホテル名" ) );
                    row.put( "住所", retSet.getString( "住所" ) );
                    row.put( "電話番号1", retSet.getString( "電話番号1" ) );
                    row.put( "電話番号2", retSet.getString( "電話番号2" ) );
                    row.put( "ホテルPR", retSet.getString( "ホテルPR" ) );
                    row.put( "アクセス", retSet.getString( "アクセス" ) );
                    row.put( "部屋数", retSet.getInt( "部屋数" ) );
                    row.put( "建物形式", retSet.getString( "建物形式" ) );
                    row.put( "駐車場", retSet.getString( "駐車場" ) );
                    row.put( "駐車場台数", retSet.getInt( "駐車場台数" ) );
                    row.put( "ハイルーフ", retSet.getString( "ハイルーフ" ) );
                    row.put( "メンバー特典", retSet.getString( "メンバー特典" ) );
                    row.put( "ルームサービス", retSet.getString( "ルームサービス" ) );
                    row.put( "クレジット", retSet.getString( "クレジット" ) );
                    row.put( "クレジット種類", retSet.getString( "クレジット種類" ) );
                    row.put( "途中外出", retSet.getString( "途中外出" ) );
                    row.put( "クーポン", retSet.getString( "クーポン" ) );
                    row.put( "利用人数", retSet.getString( "利用人数" ) );
                    row.put( "予約", retSet.getString( "予約" ) );
                    row.put( "緯度", retSet.getString( "緯度" ) );
                    row.put( "経度", retSet.getString( "経度" ) );
                    row.put( "外観（ロゴ）画像", retSet.getString( "外観（ロゴ）画像" ) );

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
