package jp.happyhotel.util;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.nimbusds.jwt.JWTClaimsSet;

/**
 * �A�N�Z�X�g�[�N�������N���X<br>
 * <br>
 * �A�N�Z�X�g�[�N���֘A�̋@�\���܂Ƃ߂����[�e���e�B�N���X�ł��B<br>
 * �A�N�Z�X�g�[�N���̔��s�⌟�؂̋@�\��񋟂��܂��B<br>
 * <br>
 * �g�[�N���̔閧���̓v���p�e�B�t�@�C���itoken.properties�j���ɐݒ肵�Ă��������B<br>
 * �v���p�e�B�t�@�C���̓N���X�Ɠ����K�w�ɔz�u���Ă����K�v������܂��B<br>
 * <br>
 * ��F<br>
 * &emsp;{@code accessTokenSecretKey=<256-bit (32-byte) string>}<br>
 * 
 * @author koshiba-y1
 */
public final class AccessToken
{
    /** ���L�閧�� */
    private static final String SECRET_KEY    = PropertyReader.readStr( "accessTokenSecretKey", "token", AccessToken.class.getResource( "" ) );

    /** �g�[�N�����s�ҏ�� */
    private static final String ISSUER        = "happyhotel.jp";

    /** JWT�N���[���p������i���[�UID�j */
    public static final String  USER_ID_CLAIM = "user_id";

    /**
     * �R���X�g���N�^
     */
    private AccessToken()
    {
    }

    /**
     * �A�N�Z�X�g�[�N�����s<br>
     * <br>
     * ���ݎ�������w�莞�Ԃ����L���ȃA�N�Z�X�g�[�N���𔭍s���܂��B<br>
     * �A�N�Z�X�g�[�N���ɂ́A���s�ҁA���s�����A�L�������A���[�U�[ID���i�[����܂��B<br>
     * 
     * @param userId ���[�UID
     * @param minute �g�[�N����L���ɂ�����ԁi���j
     * @return �A�N�Z�X�g�[�N��
     * @throws IllegalArgumentException JWS�I�u�W�F�N�g�ɏ����ł��Ȃ������ꍇ
     */
    public static String issue(String userId, long minute)
    {
        Date nowDate = new Date();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer( ISSUER )
                .issueTime( nowDate )
                .expirationTime( new Date( nowDate.getTime() + (minute * 60 * 1000) ) )
                .claim( USER_ID_CLAIM, userId )
                .build();

        return JsonWebToken.issue( claimsSet, SECRET_KEY );
    }

    /**
     * �A�N�Z�X�g�[�N�����s<br>
     * <br>
     * ���ݎ�������1���Ԃ����L���ȃA�N�Z�X�g�[�N���𔭍s���܂��B<br>
     * �A�N�Z�X�g�[�N���ɂ́A���s�ҁA���s�����A�L�������A���[�UID���i�[����܂��B<br>
     * 
     * @param userId ���[�UID
     * @return �A�N�Z�X�g�[�N��
     * @throws IllegalArgumentException JWS�I�u�W�F�N�g�ɏ����ł��Ȃ������ꍇ
     */
    public static String issue(String userId)
    {
        long minute = 60;
        return issue( userId, minute );
    }

    /**
     * �A�N�Z�X�g�[�N������<br>
     * <br>
     * ���L���ڂɂ��A�A�N�Z�X�g�[�N�������؂��܂��B<br>
     * �E�������閧���ŃG���R�[�h����Ă��邩�ǂ���<br>
     * �E���s�҂��������ݒ肳��Ă��邩�ǂ���<br>
     * �E���s�������ݒ肳��Ă��邩�ǂ���<br>
     * �E�L���������ݒ肳��Ă��邩�ǂ����i�L�����������ǂ����͌��ؑΏۊO�j<br>
     * �E���[�UID���ݒ肳��Ă��邩�ǂ���<br>
     * 
     * @param token �A�N�Z�X�g�[�N��
     * @return ���،��ʂ������u�[���l
     */
    public static boolean verify(String token)
    {
        // �閧���ɂ�錟��
        if ( !JsonWebToken.verifyBySecretKey( token, SECRET_KEY ) )
        {
            return false;
        }

        // �n�s�z�e�����s�����g�[�N�����ǂ����̌���
        if ( !JsonWebToken.verifyByIssuer( token, ISSUER ) )
        {
            return false;
        }

        // ���s�������ݒ肳��Ă��邩�ǂ����̌���
        if ( !JsonWebToken.verifyByIssueTime( token ) )
        {
            return false;
        }

        // �L���������ݒ肳��Ă��邩�ǂ����̌���
        if ( !JsonWebToken.verifyByExpirationTime( token ) )
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
     * �A�N�Z�X�g�[�N���̗L�������𔻒肵�܂��B<br>
     * �A�N�Z�X�g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * �L���������ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃA�N�Z�X�g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �A�N�Z�X�g�[�N��
     * @return �A�N�Z�X�g�[�N�����L�����������ǂ����������u�[���l
     * @throws IllegalArgumentException �A�N�Z�X�g�[�N���ɗL���������ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static boolean isWithinExpirationTime(String token)
    {
        return JsonWebToken.isWithinExpirationTime( token );
    }

    /**
     * ���[�UID�擾<br>
     * <br>
     * �A�N�Z�X�g�[�N���Ɋi�[����Ă��郆�[�UID���擾���܂��B<br>
     * �A�N�Z�X�g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * ���[�UID���i�[����Ă��Ȃ��i�󕶎����܂ށj�A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃA�N�Z�X�g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �A�N�Z�X�g�[�N��
     * @return ���[�UID
     * @throws IllegalArgumentException �A�N�Z�X�g�[�N���Ƀ��[�UID���ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
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
     * �A�N�Z�X�g�[�N�����؁i���[�UID�j<br>
     * <br>
     * �A�N�Z�X�g�[�N���Ƀ��[�UID���ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * ���[�UID��null�������͋󕶎��̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �A�N�Z�X�g�[�N��
     * @return �A�N�Z�X�g�[�N���Ƀ��[�UID���ݒ肳��Ă��邩�ǂ����������u�[���l
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
}