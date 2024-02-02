package jp.happyhotel.others;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 外部サービス連携用情報提供抽象クラス<br>
 * <br>
 * Yahoo!やナビタイムなどに提供する情報を抽出するための共通機能を格納したクラスです。<br>
 * 
 * @author koshiba-y1
 */
public abstract class AbstractInformationProvider implements InformationProvider
{
    /**
     * シリアライザ
     * 
     * @author koshiba-y1
     */
    protected static interface Serializer
    {
        /**
         * オブジェクトのシリアライズ。
         * 
         * @param data 複数のデータを格納した行データ
         * @return シリアライズされたデータ
         */
        String serialize(List<Map<String, Object>> data);
    }

    /**
     * JSONシリアライザ
     * 
     * @author koshiba-y1
     */
    protected static class JsonSerializer implements Serializer
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String serialize(List<Map<String, Object>> data)
        {
            ObjectMapper mapper = new ObjectMapper()
                    .enable( SerializationFeature.INDENT_OUTPUT );

            String json;
            try
            {
                json = mapper.writeValueAsString( data );
            }
            catch ( JsonProcessingException e )
            {
                throw new IllegalArgumentException( "JSON文字列に変換できませんでした。", e );
            }

            return json;
        }
    }

    /**
     * CSVシリアライザ
     * 
     * @author koshiba-y1
     */
    protected static class CsvSerializer implements Serializer
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String serialize(List<Map<String, Object>> data)
        {
            if ( data.isEmpty() )
                return "";

            StringBuffer stringBuffer = new StringBuffer();

            final Set<String> columnSet = data.get( 0 ).keySet();
            final Pattern pattern = Pattern.compile( "[\",\r\n]" );

            // カラム名の抽出。
            for( String column : columnSet )
            {
                if ( pattern.matcher( column ).find() )
                {
                    stringBuffer.append( escapeValue( column ) );
                }
                else
                {
                    stringBuffer.append( column );
                }
                stringBuffer.append( "," );
            }
            stringBuffer.deleteCharAt( stringBuffer.length() - 1 );
            stringBuffer.append( "\r\n" );

            // 実データの抽出。
            for( Map<String, Object> row : data )
            {
                if ( !columnSet.equals( row.keySet() ) )
                    throw new IllegalArgumentException( "行データが共通のカラムを格納していません。" );

                for( String column : columnSet )
                {
                    String val = row.get( column ).toString();
                    if ( pattern.matcher( val ).find() )
                    {
                        stringBuffer.append( escapeValue( val ) );
                    }
                    else
                    {
                        stringBuffer.append( val );
                    }
                    stringBuffer.append( "," );
                }
                stringBuffer.deleteCharAt( stringBuffer.length() - 1 );
                stringBuffer.append( "\r\n" );
            }

            return stringBuffer.toString();
        }

        /**
         * {@code '"'}のエスケープおよび、{@code '"'}による囲み処理。
         * 
         * @param raw 変換前の文字列
         * @return 変換後の文字列
         */
        private static String escapeValue(String raw)
        {
            return "\"" + raw.replace( "\"", "\"\"" ) + "\"";
        }
    }

    /**
     * イレギュラー対応。<br>
     * <br>
     * ・情報提供無し<br>
     * 23100156 ホテル41<br>
     * 611589 リッツ<br>
     * 542507 Coo<br>
     * <br>
     * ・名称変更<br>
     * 25901071 HOTEL THE HOTEL<br>
     * <br>
     * ・データ確認<br>
     * 780148 北物語<br>
     * <br>
     * ・HTML特殊文字削除<br>
     * 「12L」の 「&#8467;」を「L」に変換<br>
     * 
     * @param rows 提携用の情報
     * @return 加工済みの提携用の情報
     */
    protected static List<Map<String, Object>> irregularCompliant(List<Map<String, Object>> rows)
    {
        List<Map<String, Object>> newRows = new ArrayList<Map<String, Object>>();

        for( Map<String, Object> row : rows )
        {
            int hotelId = Integer.parseInt( row.get( "id" ).toString() );

            // 情報提供なしのデータを省く。
            if ( hotelId == 23100156 || hotelId == 611589 || hotelId == 542507 )
                continue;

            // HOTEL THE HOTELの名前変更。
            if ( hotelId == 25901071 )
                row.put( "ホテル名", row.get( "ホテル名" ).toString() + "（ホテルザホテル）" );

            // データ確認。
            if ( hotelId == 780148 )
            {
                String numberOfPeople = row.get( "利用人数" ).toString();
                if ( !(numberOfPeople.contains( "3人可" ) && numberOfPeople.contains( "1人のみ可" )) )
                    throw new UnsupportedOperationException( "780148（北物語）が「3人可・1人のみ可」になっていません。" );
            }

            // HTML特殊文字削除。
            for( String key : row.keySet() )
            {
                // 文字列以外は不要。
                if ( !(row.get( key ) instanceof String) )
                    continue;

                String str = row.get( key ).toString();
                row.put( key, unescapeHtml( str ) );
            }

            newRows.add( row );
        }

        return newRows;
    }

    /**
     * HTML特殊文字対応。<br>
     * <br>
     * 基本的に削除。<br>
     * 下記のものは特別に変換。<br>
     * 「&#8467;」 ⇒ 「L」<br>
     * 「&#12316;」 ⇒ 「～」<br>
     * 「&#10010;」 ⇒ 「＋」<br>
     * <br>
     * {@link org.apache.commons.lang3.StringEscapeUtils#unescapeHtml4(String input)}などもありますが、<br>
     * 環境依存文字に変換されてしまうため、それはそれで問題がありそうなので自力で変換することにしました。<br>
     * 
     * @param raw 処理前文字列
     * @return 処理済み文字列
     */
    protected static String unescapeHtml(String raw)
    {
        raw = raw.replace( "&#8467;", "L" );
        raw = raw.replace( "&#12316;", "～" );
        raw = raw.replace( "&#10010;", "＋" );
        raw = raw.replaceAll( "&#\\d{3,};", "" );

        return raw;
    }
}
