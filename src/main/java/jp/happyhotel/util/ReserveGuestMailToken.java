package jp.happyhotel.util;

import java.util.Date;

import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHhRsvSystemConf;

import com.nimbusds.jwt.JWTClaimsSet;

/**
 * �Q�X�g���[���F�؃g�[�N�������N���X<br>
 * <br>
 * �Q�X�g���[���F�؃g�[�N���֘A�̋@�\���܂Ƃ߂����[�e���e�B�N���X�ł��B<br>
 * �Q�X�g���[���F�؃g�[�N���̔��s�⌟�؂̋@�\��񋟂��܂��B<br>
 * <br>
 * �g�[�N���̔閧����hh_rsv_system_conf�ɓo�^�����l���g�p���Ă��������B(ctrl_id1=15, ctrl_id2=1)<br>
 * <br>
 * ��F<br>
 * &emsp;{@code reserveTokenSecretKey=<256-bit (32-byte) string>}<br>
 * 
 * @author mitsuhashi-k1
 */
public final class ReserveGuestMailToken
{
    /** ���L�閧�� */
    private static String       secretKey             = getSecretKey();

    /** �g�[�N�����s�ҏ�� */
    private static final String ISSUER                = "happyhotel.jp";

    /** JWT�N���[���p������i�z�e��ID�j */
    public static final String  ID_CLAIM              = "id";
    /** JWT�N���[���p������i���\��ԍ��j */
    public static final String  RESERVE_TEMP_NO_CLAIM = "reserve_temp_no";

    /**
     * �R���X�g���N�^
     */
    private ReserveGuestMailToken()
    {
    }

    /**
     * �Q�X�g���[���F�؃g�[�N�����s<br>
     * <br>
     * ���ݎ�������w�莞�Ԃ����L���ȃQ�X�g���[���F�؃g�[�N���𔭍s���܂��B<br>
     * �Q�X�g���[���F�؃g�[�N���ɂ́A���s�ҁA���s�����A�L�������A�z�e��ID�A���\��ԍ����i�[����܂��B<br>
     * 
     * @param id �z�e��ID
     * @param reserveTemoNo ���\��ԍ�
     * @param minute �g�[�N����L���ɂ�����ԁi���j
     * @return �Q�X�g���[���F�؃g�[�N��
     * @throws IllegalArgumentException JWS�I�u�W�F�N�g�ɏ����ł��Ȃ������ꍇ
     */
    public static String issue(int id, long reserveTempNo)
    {
        Date nowDate = new Date();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer( ISSUER )
                .issueTime( nowDate )
                .claim( ID_CLAIM, id )
                .claim( RESERVE_TEMP_NO_CLAIM, reserveTempNo )
                .build();

        return JsonWebToken.issue( claimsSet, secretKey );
    }

    /**
     * �Q�X�g���[���F�؏��g�[�N������<br>
     * <br>
     * ���L���ڂɂ��A�Q�X�g���[���F�؃g�[�N�������؂��܂��B<br>
     * �E�������閧���ŃG���R�[�h����Ă��邩�ǂ���<br>
     * �E���s�҂��������ݒ肳��Ă��邩�ǂ���<br>
     * �E���s�������ݒ肳��Ă��邩�ǂ���<br>
     * �E�L���������ݒ肳��Ă��邩�ǂ����i�L�����������ǂ����͌��ؑΏۊO�j<br>
     * �E���[�UID���ݒ肳��Ă��邩�ǂ���<br>
     * 
     * @param token �Q�X�g���[���F�؃g�[�N��
     * @return ���،��ʂ������u�[���l
     */
    public static boolean verify(String token)
    {
        // �閧���ɂ�錟��
        Logging.info( "�閧���ɂ�錟��! secretKey=" + secretKey, "verify" );
        if ( !JsonWebToken.verifyBySecretKey( token, secretKey ) )
        {
            return false;
        }

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

        // ���\��ԍ����ݒ肳��Ă��邩�ǂ����̌���
        if ( !verifyByReserveTempNo( token ) )
        {
            return false;
        }

        return true;
    }

    /**
     * �L���������� <br>
     * <br>
     * �Q�X�g���[���F�؃g�[�N���̗L�������𔻒肵�܂��B<br>
     * �Q�X�g���[���F�؃g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * �L���������ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃQ�X�g���[���F�؃g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �Q�X�g���[���F�؃g�[�N��
     * @return �Q�X�g���[���F�؃g�[�N�����L�����������ǂ����������u�[���l
     * @throws IllegalArgumentException �Q�X�g���[���F�؃g�[�N���ɗL���������ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static boolean isWithinExpirationTime(String token)
    {
        return JsonWebToken.isWithinExpirationTime( token );
    }

    /**
     * �z�e��ID�擾<br>
     * <br>
     * �Q�X�g���[���F�؃g�[�N���Ɋi�[����Ă���z�e��ID���擾���܂��B<br>
     * �Q�X�g���[���F�؃g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * �z�e��ID���i�[����Ă��Ȃ��i�󕶎����܂ށj�A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃQ�X�g���[���F�؃g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �Q�X�g���[���F�؃g�[�N��
     * @return �z�e��ID
     * @throws IllegalArgumentException �Q�X�g���[���F�؃g�[�N���Ƀz�e��ID���ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
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
     * ���\��ԍ��擾<br>
     * <br>
     * �Q�X�g���[���F�؃g�[�N���Ɋi�[����Ă��鉼�\��ԍ����擾���܂��B<br>
     * �Q�X�g���[���F�؃g�[�N�������������̂��ǂ����͌��؂���܂���B<br>
     * ���\��ԍ����i�[����Ă��Ȃ��i�󕶎����܂ށj�A�������͎擾�Ɏ��s�����ꍇ�AIllegalArgumentException�𓊂��܂��B<br>
     * ��O�̔���������������ꍇ�́A{@link #verify(String token)}��p���Ď��O�ɃQ�X�g���[���F�؃g�[�N���̌��؂��s���Ă��������B<br>
     * 
     * @param token �Q�X�g���[���F�؃g�[�N��
     * @return ���\��ԍ�
     * @throws IllegalArgumentException �Q�X�g���[���F�؃g�[�N���ɉ��\��ԍ����ݒ肳��Ă��Ȃ��A�������͎擾�Ɏ��s�����ꍇ
     */
    public static Long getReserveTempNo(String token)
    {
        Long reserveTempNo = JsonWebToken.getLongClaim( token, RESERVE_TEMP_NO_CLAIM );

        if ( reserveTempNo == null )
        {
            throw new IllegalArgumentException( "token�ɂ͉��\��ԍ����ݒ肳��Ă��܂���B" );
        }

        return reserveTempNo;
    }

    /**
     * �Q�X�g���[���F�؃g�[�N�����؁i�z�e��ID�j<br>
     * <br>
     * �Q�X�g���[���F�؃g�[�N���Ƀz�e��ID���ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * �z�e��ID��null�������͋󕶎��̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �Q�X�g���[���F�؃g�[�N��
     * @return �Q�X�g���[���F�؃g�[�N���Ƀz�e��ID���ݒ肳��Ă��邩�ǂ����������u�[���l
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
     * �Q�X�g���[���F�؃g�[�N������(���\��ԍ��j<br>
     * <br>
     * �Q�X�g���[���F�؃g�[�N���ɉ��\��ԍ����ݒ肳��Ă��邩�ǂ��������؂��܂��B<br>
     * ���\��ԍ���null�������͋󕶎��̏ꍇ�A�ݒ肳��Ă��Ȃ����̂Ɣ��肳��܂��B<br>
     * 
     * @param token �Q�X�g���[���F�؃g�[�N��
     * @return �Q�X�g���[���F�؃g�[�N���ɉ��\��ԍ����ݒ肳��Ă��邩�ǂ����������u�[���l
     */
    public static boolean verifyByReserveTempNo(String token)
    {
        try
        {
            Long reserveTempNo = JsonWebToken.getLongClaim( token, RESERVE_TEMP_NO_CLAIM );

            if ( reserveTempNo == null )
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
