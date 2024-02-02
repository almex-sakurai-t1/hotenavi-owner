package jp.happyhotel.util;

import java.util.Date;

import jp.happyhotel.data.DataHhRsvSystemConf;

import com.nimbusds.jwt.JWTClaimsSet;

/**
 * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�������N���X<br>
 * <br>
 * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���֘A�̋@�\���܂Ƃ߂����[�e���e�B�N���X�ł��B<br>
 * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���̔��s�⌟�؂̋@�\��񋟂��܂��B<br>
 * <br>
 * �g�[�N���̔閧����hh_rsv_system_conf�ɓo�^�����l���g�p���Ă��������B(ctrl_id1=15, ctrl_id2=1)<br>
 * <br>
 * ��F<br>
 * &emsp;{@code reserveTokenSecretKey=<256-bit (32-byte) string>}<br>
 * 
 * @author mitsuhashi-k1
 */
public final class StayConciergeMemberMailToken
{
    /** ���L�閧�� */
    private static String       secretKey       = getSecretKey();

    /** �g�[�N�����s�ҏ�� */
    private static final String ISSUER          = "happyhotel.jp";

    /** JWT�N���[���p������imd5�j */
    public static final String  MD5_CLAIM       = "md5";
    /** JWT�N���[���p������i�z�e��ID�j */
    public static final String  HOTEL_ID_CLAIM  = "hotel_id";
    /** JWT�N���[���p������i���[�U�[ID�j */
    public static final String  USER_ID_CLAIM   = "user_id";
    /** JWT�N���[���p������i�J�X�^��ID�j */
    public static final String  CUSTOM_ID_CLAIM = "custom_id";
    /** JWT�N���[���p������i�a����1�i���j�j */
    public static final String  BIRTHDAY1_CLAIM = "birthday1";
    /** JWT�N���[���p������i�a����2�i���j�j */
    public static final String  BIRTHDAY2_CLAIM = "birthday2";

    /**
     * �R���X�g���N�^
     */
    private StayConciergeMemberMailToken()
    {
    }

    /**
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�����s<br>
     * <br>
     * ���ݎ�������w�莞�Ԃ����L���ȃX�e�C�R���V�F���W������o�^���[���F�؃g�[�N���𔭍s���܂��B<br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���ɂ́A���s�ҁA���s�����A�L�������Amd5�A���[�U�[ID���i�[����܂��B<br>
     * 
     * @param md5 m_member.md5
     * @param userId ���[�U�[ID
     * @param customId �J�X�^��ID
     * @param birthday1 �a����1(��)
     * @param birthday2 �a����2�i���j
     * @param minute �g�[�N����L���ɂ�����ԁi���j
     * @return �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @throws IllegalArgumentException JWS�I�u�W�F�N�g�ɏ����ł��Ȃ������ꍇ
     */
    public static String issue(String md5, String hotelId, String userId, String customId, String birthday1, String birthday2)
    {
        Date nowDate = new Date();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer( ISSUER )
                .issueTime( nowDate )
                .claim( MD5_CLAIM, md5 )
                .claim( HOTEL_ID_CLAIM, hotelId )
                .claim( USER_ID_CLAIM, userId )
                .claim( CUSTOM_ID_CLAIM, customId )
                .claim( BIRTHDAY1_CLAIM, birthday1 )
                .claim( BIRTHDAY2_CLAIM, birthday2 )
                .build();

        return JsonWebToken.issue( claimsSet, secretKey );
    }

    /**
     * �X�e�C�R���V�F���W������o�^���[���F�؏��g�[�N������<br>
     * <br>
     * ���L���ڂɂ��A�X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�������؂��܂��B<br>
     * �E�������閧���ŃG���R�[�h����Ă��邩�ǂ���<br>
     * �E���s�҂��������ݒ肳��Ă��邩�ǂ���<br>
     * �E���s�������ݒ肳��Ă��邩�ǂ���<br>
     * �E�L���������ݒ肳��Ă��邩�ǂ����i�L�����������ǂ����͌��ؑΏۊO�j<br>
     * �E���[�UID���ݒ肳��Ă��邩�ǂ���<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return ���،��ʂ������u�[���l
     */
    public static boolean verify(String token)
    {
        // �閧���ɂ�錟��
        if ( !JsonWebToken.verifyBySecretKey( token, secretKey ) )
        {
            return false;
        }

        // �n�s�z�e�����s�����g�[�N�����ǂ����̌���
        if ( !JsonWebToken.verifyByIssuer( token, ISSUER ) )
        {
            return false;
        }

        // md5���ݒ肳��Ă��邩�ǂ����̌���
        if ( !verifyByMd5( token ) )
        {
            return false;
        }

        // �z�e��ID���ݒ肳��Ă��邩�ǂ����̌���
        if ( !verifyByHotelId( token ) )
        {
            return false;
        }

        // ���[�U�[ID���ݒ肳��Ă��邩�ǂ����̌���
        if ( !verifyByUserId( token ) )
        {
            return false;
        }

        // �J�X�^��ID���ݒ肳��Ă��邩�ǂ����̌���
        if ( !verifyByCustomId( token ) )
        {
            return false;
        }

        // �a����1���ݒ肳��Ă��邩�ǂ����̌���
        if ( !verifyByBirthday1( token ) )
        {
            return false;
        }

        // �a����2���ݒ肳��Ă��邩�ǂ����̌���
        if ( !verifyByBirthday2( token ) )
        {
            return false;
        }

        return true;
    }

    /**
     * �L���������� <br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���̗L�������𔻒肵�܂��B<br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * �L���������ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃX�e�C�R���V�F���W������o�^���[���F�؃g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�����L�����������ǂ����������u�[���l
     * @throws IllegalArgumentException �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���ɗL���������ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static boolean isWithinExpirationTime(String token)
    {
        return JsonWebToken.isWithinExpirationTime( token );
    }

    /**
     * md5�擾<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ɋi�[����Ă���md5���擾���܂��B<br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * md5���i�[����Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃX�e�C�R���V�F���W������o�^���[���F�؃g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return md5
     * @throws IllegalArgumentException �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N����md5���ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static String getMd5(String token)
    {
        String md5 = JsonWebToken.getStringClaim( token, MD5_CLAIM );

        if ( md5 == null )
        {
            throw new IllegalArgumentException( "token�ɂ�MD5���ݒ肳��Ă��܂���B" );
        }

        return md5;
    }

    /**
     * �z�e��ID�擾<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ɋi�[����Ă���z�e��ID���擾���܂��B<br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * �z�e��ID���i�[����Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃX�e�C�R���V�F���W������o�^���[���F�؃g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return �z�e��ID
     * @throws IllegalArgumentException �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ƀ��[�U�[ID���ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static String getHotelId(String token)
    {
        String userId = JsonWebToken.getStringClaim( token, HOTEL_ID_CLAIM );

        if ( userId == null )
        {
            throw new IllegalArgumentException( "token�ɂ̓z�e��ID���ݒ肳��Ă��܂���B" );
        }

        return userId;
    }

    /**
     * ���[�U�[ID�擾<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ɋi�[����Ă��郆�[�U�[ID���擾���܂��B<br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * ���[�U�[ID���i�[����Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃX�e�C�R���V�F���W������o�^���[���F�؃g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return ���[�U�[ID
     * @throws IllegalArgumentException �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ƀ��[�U�[ID���ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static String getUserId(String token)
    {
        String userId = JsonWebToken.getStringClaim( token, USER_ID_CLAIM );

        if ( userId == null )
        {
            throw new IllegalArgumentException( "token�ɂ̓��[�U�[ID���ݒ肳��Ă��܂���B" );
        }

        return userId;
    }

    /**
     * �J�X�^��ID�擾<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ɋi�[����Ă���J�X�^��ID���擾���܂��B<br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * �J�X�^��ID���i�[����Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃX�e�C�R���V�F���W������o�^���[���F�؃g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return �J�X�^��ID
     * @throws IllegalArgumentException �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ƀ��[�U�[ID���ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static String getCustomId(String token)
    {
        String customId = JsonWebToken.getStringClaim( token, CUSTOM_ID_CLAIM );

        if ( customId == null )
        {
            throw new IllegalArgumentException( "token�ɂ̓J�X�^��ID���ݒ肳��Ă��܂���B" );
        }

        return customId;
    }

    /**
     * �a����1�擾<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ɋi�[����Ă���a����1���擾���܂��B<br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * �a����1���i�[����Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃX�e�C�R���V�F���W������o�^���[���F�؃g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return �a����1
     * @throws IllegalArgumentException �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ƀ��[�U�[ID���ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static String getBirthday1(String token)
    {
        String birthday1 = JsonWebToken.getStringClaim( token, BIRTHDAY1_CLAIM );

        if ( birthday1 == null )
        {
            throw new IllegalArgumentException( "token�ɂ͒a����1���ݒ肳��Ă��܂���B" );
        }

        return birthday1;
    }

    /**
     * �a����2�擾<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ɋi�[����Ă���a����2���擾���܂��B<br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * �a����2���i�[����Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃX�e�C�R���V�F���W������o�^���[���F�؃g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return �a����2
     * @throws IllegalArgumentException �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ƀ��[�U�[ID���ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static String getBirthday2(String token)
    {
        String birthday2 = JsonWebToken.getStringClaim( token, BIRTHDAY2_CLAIM );

        if ( birthday2 == null )
        {
            throw new IllegalArgumentException( "token�ɂ͒a����2���ݒ肳��Ă��܂���B" );
        }

        return birthday2;
    }

    /**
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N�����؁imd5�j<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N����md5���ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * md5��null�̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N����md5���ݒ肳��Ă��邩�ǂ����������u�[���l
     */
    public static boolean verifyByMd5(String token)
    {
        try
        {
            String md5 = JsonWebToken.getStringClaim( token, MD5_CLAIM );

            if ( md5 == null )
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
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N������(�z�e��ID�j<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ƀ��[�U�[ID���ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * �z�e��ID��null�̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ƀz�e��ID���ݒ肳��Ă��邩�ǂ����������u�[���l
     */
    public static boolean verifyByHotelId(String token)
    {
        try
        {
            String hotelId = JsonWebToken.getStringClaim( token, HOTEL_ID_CLAIM );

            if ( hotelId == null )
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
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N������(���[�U�[ID�j<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ƀ��[�U�[ID���ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * ���[�U�[ID��null�̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���Ƀ��[�U�[ID���ݒ肳��Ă��邩�ǂ����������u�[���l
     */
    public static boolean verifyByUserId(String token)
    {
        try
        {
            String userId = JsonWebToken.getStringClaim( token, USER_ID_CLAIM );

            if ( userId == null )
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
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N������(�J�X�^��ID�j<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���ɃJ�X�^��ID���ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * �J�X�^��ID��null�̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���ɃJ�X�^��ID���ݒ肳��Ă��邩�ǂ����������u�[���l
     */
    public static boolean verifyByCustomId(String token)
    {
        try
        {
            String customId = JsonWebToken.getStringClaim( token, CUSTOM_ID_CLAIM );

            if ( customId == null )
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
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N������(�a����1�j<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���ɒa����1���ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * �a����1��null�̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���ɒa����1���ݒ肳��Ă��邩�ǂ����������u�[���l
     */
    public static boolean verifyByBirthday1(String token)
    {
        try
        {
            String birthday1 = JsonWebToken.getStringClaim( token, BIRTHDAY1_CLAIM );

            if ( birthday1 == null )
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
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N������(�a����2�j<br>
     * <br>
     * �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���ɒa����2���ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * �a����1��null�̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N��
     * @return �X�e�C�R���V�F���W������o�^���[���F�؃g�[�N���ɒa����2���ݒ肳��Ă��邩�ǂ����������u�[���l
     */
    public static boolean verifyByBirthday2(String token)
    {
        try
        {
            String birthday2 = JsonWebToken.getStringClaim( token, BIRTHDAY2_CLAIM );

            if ( birthday2 == null )
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
