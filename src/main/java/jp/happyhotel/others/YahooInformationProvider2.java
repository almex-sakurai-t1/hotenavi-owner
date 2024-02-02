package jp.happyhotel.others;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;

import org.apache.commons.lang.StringUtils;

/**
 * Yahoo!連携用情報（ホテル部屋画像）提供クラス。
 * 
 * @author koshiba-y1
 */
public class YahooInformationProvider2 extends AbstractInformationProvider
{
    /** 提携情報格納用オブジェクト */
    List<Map<String, Object>> data;

    /**
     * コンストラクタ。
     */
    public YahooInformationProvider2()
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
            rows = selectHotelImgData();
        }
        catch ( Exception e )
        {
            Logging.warn( "Exception e=" + e.toString() );
            throw new UnsupportedOperationException( e );
        }

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
     * ホテル部屋画像情報の取得。
     * 
     * @return 提携用の情報
     */
    private static List<Map<String, Object>> selectHotelImgData() throws Exception
    {
        final Set<Integer> hotelSet = selectRank2OrMoreHotelSet();

        final String happyhotelUrl = Url.getUrl();
        if ( StringUtils.isEmpty( happyhotelUrl ) )
            throw new UnsupportedOperationException( "プロパティファイルからハッピーホテルのURLを取得できませんでした。" );

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

        final String query = ""
                + "SELECT "
                + "    hh_hotel_room.id AS `id`, "
                + "    CONCAT( "
                + "        '" + happyhotelUrl + "/servlet/HotelRoomPicture', "
                + "        '?id=', hh_hotel_room.id, "
                + "        '&seq=', hh_hotel_room.seq "
                + "    ) AS `部屋画像` "
                + "FROM "
                + "    hh_hotel_room "
                + "WHERE "
                + "    hh_hotel_room.disp_flag = 1 "
                + "ORDER BY "
                + "    hh_hotel_room.id, "
                + "    hh_hotel_room.seq;";

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
                    int hotelId = retSet.getInt( "id" );

                    // ランク1以下のホテルは抽出しない（SQL側で処理するとスローになるためJava側で対応）。
                    if ( !hotelSet.contains( hotelId ) )
                        continue;

                    // 情報提供なしのホテルのデータを省く。
                    if ( hotelId == 23100156 || hotelId == 611589 || hotelId == 542507 )
                        continue;

                    Map<String, Object> row = new LinkedHashMap<String, Object>();

                    row.put( "id", hotelId );
                    row.put( "部屋画像", retSet.getString( "部屋画像" ) );

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

    /**
     * ホテルセットの取得。
     * 
     * @return ランク2以上で、種類が7以下、テストホテルを省いたホテルのホテルIDを格納したセット
     */
    private static Set<Integer> selectRank2OrMoreHotelSet() throws Exception
    {
        Set<Integer> hotelSet = new HashSet<Integer>();

        final String query = ""
                + "SELECT "
                + "    hh_hotel_basic.id AS `id` "
                + "FROM "
                + "    hh_hotel_basic "
                + "WHERE "
                + "    99999 < hh_hotel_basic.id AND hh_hotel_basic.id < 89999999 "
                + "    AND hh_hotel_basic.rank >= 2 "
                + "    AND hh_hotel_basic.kind <= 7;";

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
                    hotelSet.add( retSet.getInt( "id" ) );
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

        return hotelSet;
    }
}
