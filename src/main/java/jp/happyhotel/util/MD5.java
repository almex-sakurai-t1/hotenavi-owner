package jp.happyhotel.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5�ϊ��Ɋւ���@�\��񋟂���N���X
 * 
 * @author mitsuhashi-k1
 */
public class MD5
{
    /**
     * �R���X�g���N�^
     */
    private MD5()
    {
    }

    /**
     * MD5�ϊ��i16�i���j<br>
     * <br>
     * �w�肵���������MD5�l�ɕϊ����܂��B<br>
     * ������null�������ꍇNullPointerException���������܂��B<br>
     * 
     * @param source MD5�l�ɕϊ����镶����
     * @return MD5�l
     * @throws NullPointerException ������null�������ꍇ�B
     */

    public static String convert(String source)
    {
        if ( source == null )
        {
            throw new NullPointerException();
        }

        MessageDigest messageDigest;
        try
        {
            messageDigest = MessageDigest.getInstance( "MD5" );
        }
        catch ( NoSuchAlgorithmException e )
        {
            throw new UnsupportedOperationException( e );
        }

        byte[] byteArray = source.getBytes();
        messageDigest.update( byteArray );
        byteArray = messageDigest.digest();

        StringBuilder strBuilder = new StringBuilder();
        for( int i = 0 ; i < byteArray.length ; i++ )
        {
            int val = byteArray[i] & 0xff;

            if ( val < 16 )
            {
                strBuilder.append( "0" );
            }

            // radix��N�̏ꍇ�A�����̕����̐擪����N���A���������ɊN�̌��Ƃ��Ďg�p����܂��B
            // ���Ȃ킿�A16�i���i���16�j�Ɏg���錅�̐�����0123456789abcdef�ƂȂ�܂��B
            strBuilder.append( Integer.toString( val, 16 ) );
        }

        String md5Hash = strBuilder.toString();

        return md5Hash;
    }

    /**
     * MD5�ϊ��i16�i���j<br>
     * <br>
     * �w�肵���������MD5�l�ɕϊ����܂��B<br>
     * ������null�������ꍇNullPointerException���������܂��B<br>
     * 
     * @param source MD5�l�ɕϊ����镶����
     * @param count �ϊ������
     * @return MD5�l
     * @throws NullPointerException ������null�������ꍇ�B
     */

    public static String convert(String source, int count)
    {
        if ( source == null )
        {
            throw new NullPointerException();
        }

        String md5Hash = source;

        for( int i = 0 ; i < count ; i++ )
        {
            md5Hash = convert( md5Hash );
        }
        return md5Hash;
    }

}