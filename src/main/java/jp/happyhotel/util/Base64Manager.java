package jp.happyhotel.util;

import sun.nio.cs.StandardCharsets;

/**
 * base64������encode/decode�p�N���X
 * 
 * @author paku-k1
 */
public class Base64Manager
{
    /**
     * �R���X�g���N�^
     */
    private Base64Manager()
    {
    }

    /**
     * Base64������փG���R�[�h
     * 
     * @param src Base64�ŃG���R�[�h���镶����
     * @return �G���R�[�h���ꂽ������
     */
    @SuppressWarnings("restriction")
    public static String encode(String src)
    {
        byte[] bytes = src.getBytes( StandardCharsets.UTF_8 );
        return Base64.getEncoder().encodeToString( bytes );
    }

    /**
     * Base64������̃f�R�[�h
     * 
     * @param src Base64�ŃG���R�[�h���ꂽ������
     * @return �f�R�[�h���ꂽ������
     * @throws IllegalArgumentException {@code src} ���L����Base64�X�L�[���ɂȂ��Ă��Ȃ��ꍇ
     */
    public static String decode(String src)
    {
        byte[] bytes = Base64.getDecoder().decode( src );
        return new String( bytes, StandardCharsets.UTF_8 );
    }
}
