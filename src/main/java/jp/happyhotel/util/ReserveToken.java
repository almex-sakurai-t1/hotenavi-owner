package jp.happyhotel.util;

import java.util.Date;

import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHhRsvSystemConf;

import org.apache.commons.lang.StringUtils;

import com.nimbusds.jwt.JWTClaimsSet;

/**
 * �\����g�[�N�������N���X<br>
 * <br>
 * �\����g�[�N���֘A�̋@�\���܂Ƃ߂����[�e���e�B�N���X�ł��B<br>
 * �\����g�[�N���̔��s�⌟�؂̋@�\��񋟂��܂��B<br>
 * <br>
 * �g�[�N���̔閧����hh_rsv_system_conf�ɓo�^�����l���g�p���Ă��������B(ctrl_id1=15, ctrl_id2=1)<br>
 * <br>
 * ��F<br>
 * &emsp;{@code reserveTokenSecretKey=<256-bit (32-byte) string>}<br>
 * 
 * @author mitsuhashi-k1
 */
public final class ReserveToken
{
    /** ���L�閧�� */
    private static String       secretKey        = getSecretKey();

    /** �g�[�N�����s�ҏ�� */
    private static final String ISSUER           = "happyhotel.jp";

    /** JWT�N���[���p������i�z�e��ID�j */
    public static final String  ID_CLAIM         = "id";
    /** JWT�N���[���p������i�\��ԍ��j */
    public static final String  RESERVE_NO_CLAIM = "reserve_no";
    /** JWT�N���[���p������i���[�UID�j */
    public static final String  USER_ID_CLAIM    = "user_id";

    /**
     * �R���X�g���N�^
     */
    private ReserveToken()
    {
    }

    /**
     * �\����g�[�N�����s<br>
     * <br>
     * ���ݎ�������w�莞�Ԃ����L���ȗ\����g�[�N���𔭍s���܂��B<br>
     * �\����g�[�N���ɂ́A���s�ҁA���s�����A�L�������A�z�e��ID�A�\��ԍ��A���[�UID���i�[����܂��B<br>
     * 
     * @param id �z�e��ID
     * @param reserveNo �\��ԍ�
     * @param userId ���[�UID
     * @param minute �g�[�N����L���ɂ�����ԁi���j
     * @return �\����g�[�N��
     * @throws IllegalArgumentException JWS�I�u�W�F�N�g�ɏ����ł��Ȃ������ꍇ
     */
    public static String issue(int id, String reserveNo, String userId)
    {
        Date nowDate = new Date();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer( ISSUER )
                .issueTime( nowDate )
                .claim( ID_CLAIM, id )
                .claim( RESERVE_NO_CLAIM, reserveNo )
                .claim( USER_ID_CLAIM, userId )
                .build();

        return JsonWebToken.issue( claimsSet, secretKey );
    }

    /**
     * �\����g�[�N������<br>
     * <br>
     * ���L���ڂɂ��A�\����g�[�N�������؂��܂��B<br>
     * �E�������閧���ŃG���R�[�h����Ă��邩�ǂ���<br>
     * �E���s�҂��������ݒ肳��Ă��邩�ǂ���<br>
     * �E���s�������ݒ肳��Ă��邩�ǂ���<br>
     * �E�L���������ݒ肳��Ă��邩�ǂ����i�L�����������ǂ����͌��ؑΏۊO�j<br>
     * �E���[�UID���ݒ肳��Ă��邩�ǂ���<br>
     * 
     * @param token �\����g�[�N��
     * @return ���،��ʂ������u�[���l
     */
    public static boolean verify(String token)
    {
        // �閧���ɂ�錟��
        Logging.info( "�閧���ɂ�錟��! secretKey=" + secretKey );
        if ( !JsonWebToken.verifyBySecretKey( token, secretKey ) )
        {
            return false;
        }
        Logging.info( "�n�s�z�e�����s�����g�[�N�����ǂ����̌��� ISSUER=" + ISSUER );
        // �n�s�z�e�����s�����g�[�N�����ǂ����̌���
        if ( !JsonWebToken.verifyByIssuer( token, ISSUER ) )
        {
            return false;
        }

        // �z�e��ID���ݒ肳��Ă��邩�ǂ����̌���
        if ( !verifyById( token ) )
        {
            return false;
        }
        // �\��ԍ����ݒ肳��Ă��邩�ǂ����̌���
        if ( !verifyByReserveNo( token ) )
        {
            return false;
        }
        // ���[�UID���ݒ肳��Ă��邩�ǂ����̌���
        if ( !verifyByUserId( token ) )
        {
            return false;
        }
        return true;
    }

    /**
     * �L���������� <br>
     * <br>
     * �\����g�[�N���̗L�������𔻒肵�܂��B<br>
     * �\����g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * �L���������ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɗ\����g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �\����g�[�N��
     * @return �\����g�[�N�����L�����������ǂ����������u�[���l
     * @throws IllegalArgumentException �\����g�[�N���ɗL���������ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static boolean isWithinExpirationTime(String token)
    {
        return JsonWebToken.isWithinExpirationTime( token );
    }

    /**
     * �z�e��ID�擾<br>
     * <br>
     * �\����g�[�N���Ɋi�[����Ă���z�e��ID���擾���܂��B<br>
     * �\����g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * �z�e��ID���i�[����Ă��Ȃ��i�󕶎����܂ށj�A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɗ\����g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �\����g�[�N��
     * @return �z�e��ID
     * @throws IllegalArgumentException �\����g�[�N���Ƀz�e��ID���ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static Integer getId(String token)
    {
        Integer id = JsonWebToken.getIntegerClaim( token, ID_CLAIM );

        if ( id == null )
        {
            throw new IllegalArgumentException( "token�ɂ̓z�e��ID���ݒ肳��Ă��܂���B" );
        }

        return id;
    }

    /**
     * �\��ԍ��擾<br>
     * <br>
     * �\����g�[�N���Ɋi�[����Ă���\��ԍ����擾���܂��B<br>
     * �\����g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * �\��ԍ����i�[����Ă��Ȃ��i�󕶎����܂ށj�A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɗ\����g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �\����g�[�N��
     * @return �\��ԍ�
     * @throws IllegalArgumentException �\����g�[�N���ɗ\��ԍ����ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static String getReserveNo(String token)
    {
        String reserveNo = JsonWebToken.getStringClaim( token, RESERVE_NO_CLAIM );

        if ( StringUtils.isEmpty( reserveNo ) )
        {
            throw new IllegalArgumentException( "token�ɂ͗\��ԍ����ݒ肳��Ă��܂���B" );
        }

        return reserveNo;
    }

    /**
     * ���[�UID�擾<br>
     * <br>
     * �\����g�[�N���Ɋi�[����Ă��郆�[�UID���擾���܂��B<br>
     * �\����g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * ���[�UID���i�[����Ă��Ȃ��i�󕶎����܂ށj�A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɗ\����g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �\����g�[�N��
     * @return ���[�UID
     * @throws IllegalArgumentException �\����g�[�N���Ƀ��[�UID���ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static String getUserId(String token)
    {
        String userId = JsonWebToken.getStringClaim( token, USER_ID_CLAIM );

        if ( StringUtils.isEmpty( userId ) )
        {
            throw new IllegalArgumentException( "token�ɂ̓��[�UID���ݒ肳��Ă��܂���B" );
        }

        return userId;
    }

    /**
     * �\����g�[�N�����؁i�z�e��ID�j<br>
     * <br>
     * �\����g�[�N���Ƀz�e��ID���ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * �z�e��ID��null�������͋󕶎��̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �\����g�[�N��
     * @return �\����g�[�N���Ƀz�e��ID���ݒ肳��Ă��邩�ǂ����������u�[���l
     */
    public static boolean verifyById(String token)
    {
        try
        {
            Integer id = JsonWebToken.getIntegerClaim( token, ID_CLAIM );

            if ( id == null )
            {
                return false;
            }

            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * �\����g�[�N�����؁i�\��ԍ��j<br>
     * <br>
     * �\����g�[�N���ɗ\��ԍ����ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * �\��ԍ���null�������͋󕶎��̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �\����g�[�N��
     * @return �\����g�[�N���ɗ\��ԍ����ݒ肳��Ă��邩�ǂ����������u�[���l
     */
    public static boolean verifyByReserveNo(String token)
    {
        try
        {
            String reserveNo = JsonWebToken.getStringClaim( token, RESERVE_NO_CLAIM );

            if ( StringUtils.isEmpty( reserveNo ) )
            {
                return false;
            }

            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * �\����g�[�N�����؁i���[�UID�j<br>
     * <br>
     * �\����g�[�N���Ƀ��[�UID���ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * ���[�UID��null�������͋󕶎��̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �\����g�[�N��
     * @return �\����g�[�N���Ƀ��[�UID���ݒ肳��Ă��邩�ǂ����������u�[���l
     */
    public static boolean verifyByUserId(String token)
    {
        try
        {
            String userId = JsonWebToken.getStringClaim( token, USER_ID_CLAIM );

            if ( StringUtils.isEmpty( userId ) )
            {
                return false;
            }

            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * �閧���擾<br>
     * <br>
     * �g�[�N���̔閧����hh_rsv_system_conf����擾���܂��B(ctrl_id1=15, ctrl_id2=1)<br>
     * 
     * @return �閧��
     */
    private static String getSecretKey()
    {
        String secretKey = "";
        try
        {
            // �閧����hh_rsv_system_conf����擾����B
            DataHhRsvSystemConf sysConf = new DataHhRsvSystemConf();
            if ( sysConf.getData( 15, 1 ) )
            {
                secretKey = sysConf.getVal2();
            }
            return secretKey;
        }
        catch ( Exception e )
        {
            return secretKey;
        }
    }
}
