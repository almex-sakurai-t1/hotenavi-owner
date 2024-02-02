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
 * �O���T�[�r�X�A�g�p���񋟒��ۃN���X<br>
 * <br>
 * Yahoo!��i�r�^�C���Ȃǂɒ񋟂�����𒊏o���邽�߂̋��ʋ@�\���i�[�����N���X�ł��B<br>
 * 
 * @author koshiba-y1
 */
public abstract class AbstractInformationProvider implements InformationProvider
{
    /**
     * �V���A���C�U
     * 
     * @author koshiba-y1
     */
    protected static interface Serializer
    {
        /**
         * �I�u�W�F�N�g�̃V���A���C�Y�B
         * 
         * @param data �����̃f�[�^���i�[�����s�f�[�^
         * @return �V���A���C�Y���ꂽ�f�[�^
         */
        String serialize(List<Map<String, Object>> data);
    }

    /**
     * JSON�V���A���C�U
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
                throw new IllegalArgumentException( "JSON������ɕϊ��ł��܂���ł����B", e );
            }

            return json;
        }
    }

    /**
     * CSV�V���A���C�U
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

            // �J�������̒��o�B
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

            // ���f�[�^�̒��o�B
            for( Map<String, Object> row : data )
            {
                if ( !columnSet.equals( row.keySet() ) )
                    throw new IllegalArgumentException( "�s�f�[�^�����ʂ̃J�������i�[���Ă��܂���B" );

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
         * {@code '"'}�̃G�X�P�[�v����сA{@code '"'}�ɂ��͂ݏ����B
         * 
         * @param raw �ϊ��O�̕�����
         * @return �ϊ���̕�����
         */
        private static String escapeValue(String raw)
        {
            return "\"" + raw.replace( "\"", "\"\"" ) + "\"";
        }
    }

    /**
     * �C���M�����[�Ή��B<br>
     * <br>
     * �E���񋟖���<br>
     * 23100156 �z�e��41<br>
     * 611589 ���b�c<br>
     * 542507 Coo<br>
     * <br>
     * �E���̕ύX<br>
     * 25901071 HOTEL THE HOTEL<br>
     * <br>
     * �E�f�[�^�m�F<br>
     * 780148 �k����<br>
     * <br>
     * �EHTML���ꕶ���폜<br>
     * �u12L�v�� �u&#8467;�v���uL�v�ɕϊ�<br>
     * 
     * @param rows ��g�p�̏��
     * @return ���H�ς݂̒�g�p�̏��
     */
    protected static List<Map<String, Object>> irregularCompliant(List<Map<String, Object>> rows)
    {
        List<Map<String, Object>> newRows = new ArrayList<Map<String, Object>>();

        for( Map<String, Object> row : rows )
        {
            int hotelId = Integer.parseInt( row.get( "id" ).toString() );

            // ���񋟂Ȃ��̃f�[�^���Ȃ��B
            if ( hotelId == 23100156 || hotelId == 611589 || hotelId == 542507 )
                continue;

            // HOTEL THE HOTEL�̖��O�ύX�B
            if ( hotelId == 25901071 )
                row.put( "�z�e����", row.get( "�z�e����" ).toString() + "�i�z�e���U�z�e���j" );

            // �f�[�^�m�F�B
            if ( hotelId == 780148 )
            {
                String numberOfPeople = row.get( "���p�l��" ).toString();
                if ( !(numberOfPeople.contains( "3�l��" ) && numberOfPeople.contains( "1�l�̂݉�" )) )
                    throw new UnsupportedOperationException( "780148�i�k����j���u3�l�E1�l�̂݉v�ɂȂ��Ă��܂���B" );
            }

            // HTML���ꕶ���폜�B
            for( String key : row.keySet() )
            {
                // ������ȊO�͕s�v�B
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
     * HTML���ꕶ���Ή��B<br>
     * <br>
     * ��{�I�ɍ폜�B<br>
     * ���L�̂��͓̂��ʂɕϊ��B<br>
     * �u&#8467;�v �� �uL�v<br>
     * �u&#12316;�v �� �u�`�v<br>
     * �u&#10010;�v �� �u�{�v<br>
     * <br>
     * {@link org.apache.commons.lang3.StringEscapeUtils#unescapeHtml4(String input)}�Ȃǂ�����܂����A<br>
     * ���ˑ������ɕϊ�����Ă��܂����߁A����͂���Ŗ�肪���肻���Ȃ̂Ŏ��͂ŕϊ����邱�Ƃɂ��܂����B<br>
     * 
     * @param raw �����O������
     * @return �����ςݕ�����
     */
    protected static String unescapeHtml(String raw)
    {
        raw = raw.replace( "&#8467;", "L" );
        raw = raw.replace( "&#12316;", "�`" );
        raw = raw.replace( "&#10010;", "�{" );
        raw = raw.replaceAll( "&#\\d{3,};", "" );

        return raw;
    }
}
