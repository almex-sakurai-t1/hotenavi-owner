package jp.happyhotel.common;

/**
 * �ݒ���N���X
 * 
 */
public class Constants
{
    public static final int     pageLimitRecords         = 20;
    public static final int     pageLimitRecordMobile    = 10;
    public static final int     maxRecords               = 200;
    public static final int     maxRecordsHotenavi       = 500;
    public static final int     maxRecordsMobile         = 50;

    public static String        configFilesPath          = "//etc//happyhotel//";
    public static final String  errorRecordsNotFound1    = "�w�肵���t���[���[�h�̃z�e���͌�����܂���ł����B";
    public static final String  errorRecordsNotFound2    = "������܂���ł����B";
    public static final String  errorRecordsNotFound3    = "�������ʂ�200���𒴂��܂����B�i���݂��s���Ă��������B";
    public static final String  errorRecordsNotFound4    = "�������ʂ�500���𒴂��܂����B�i���݂��s���Ă��������B";
    public static final String  errorRecordsMyHotel      = "�o�^�ς݃z�e��������܂���B";
    public static final String  errorRecordsHotelMembers = "�J�[�h���X�����o�[�o�^�ς�\n�z�e��������܂���B";
    public static final boolean zeroResultDisplay        = true;
    public static final boolean shiborikomiByPref        = false;

    public static final String  errorLimitFreeword       = "�������ʂ�" + maxRecords + "���𒴂��܂����B�t���[���[�h�̍i���݂��s���Ă��������B";
    public static final String  errorLimitRecords        = "�������ʂ�200���𒴂��܂����B�i���݂��s���Ă��������B";
    public static final String  ROOT_TAG_NAME_HAPPYHOTEL = "happyhotel";
    public static String        HAPPYHOTEL_URL           = "http://happyhotel.jp";

    public static final int     ERROR_CODE_API1          = 1;
    public static final int     ERROR_CODE_API2          = 2;
    public static final int     ERROR_CODE_API3          = 3;
    public static final int     ERROR_CODE_API4          = 4;
    public static final int     ERROR_CODE_API5          = 5;
    public static final int     ERROR_CODE_API6          = 6;
    public static final int     ERROR_CODE_API7          = 7;
    public static final int     ERROR_CODE_API8          = 8;
    public static final int     ERROR_CODE_API9          = 9;
    public static final int     ERROR_CODE_API10         = 10;
    public static final int     ERROR_CODE_API11         = 11;
    public static final int     ERROR_CODE_API12         = 12;
    public static final int     ERROR_CODE_API13         = 13;
    public static final int     ERROR_CODE_API14         = 14;
    public static final int     ERROR_CODE_API15         = 15;
    public static final int     ERROR_CODE_API16         = 16;
    public static final int     ERROR_CODE_API17         = 17;
    public static final int     ERROR_CODE_API18         = 18;
    public static final int     ERROR_CODE_API19         = 19;
    public static final int     ERROR_CODE_API20         = 20;
    public static final int     ERROR_CODE_API21         = 21;

    public static final String  ERROR_MSG_API1           = "���[�UID��������܂���ł����B";
    public static final String  ERROR_MSG_API2           = "�p�X���[�h����v���܂���B";
    public static final String  ERROR_MSG_API3           = "�����񂪎擾�ł��܂���B\n�ݒ胁�j���[����ID�E�p�X���[�h�������������m�F���������B";
    public static final String  ERROR_MSG_API4           = "�p�����[�^������������܂���B";
    public static final String  ERROR_MSG_API5           = "�p�����[�^���s�����Ă��܂��B";
    public static final String  ERROR_MSG_API6           = "�Y���z�e����0���ł��B";
    public static final String  ERROR_MSG_API7           = "�܂��N�`�R�~������܂���B";
    public static final String  ERROR_MSG_API8           = "�N�`�R�~�ł��Ȃ��z�e���ł��B";
    public static final String  ERROR_MSG_API9           = "��O���������܂����B";
    public static final String  ERROR_MSG_API10          = "�������܃A�N�Z�X���W�����Ă���܂��B���΂炭���Ԃ������Ă���ēx���������������B";
    public static final String  ERROR_MSG_API11          = "������܂���ł���";
    public static final String  ERROR_MSG_API12          = "���[�UID�܂��̓p�X���[�h����v���܂���";
    public static final String  ERROR_MSG_API13          = "���ɉ���o�^�ς݂ł�";
    public static final String  ERROR_MSG_API14          = "������o�^�Ɏ��s���܂���";
    public static final String  ERROR_MSG_API15          = "�}�C�z�e���폜�Ɏ��s���܂���";
    public static final String  ERROR_MSG_API16          = "�n�s�z�e������̐ݒ肪�K�v�ł�";
    public static final String  ERROR_MSG_API17          = "�f�o�C�X�g�[�N���̓o�^�Ɏ��s���܂���";
    public static final String  ERROR_MSG_API18          = "UUID�̓o�^�Ɏ��s���܂���";
    public static final String  ERROR_MSG_API19          = "UUID���̎擾�Ɏ��s���܂���";
    public static final String  ERROR_MSG_API20          = "���V�[�g�̌��؂Ɏ��s���܂���";
    public static final String  ERROR_MSG_API21          = "���V�[�g�̗L���������؂�Ă��܂�";
    public static final String  ERROR_MSG_API22          = "�ēx���O�C�����Ă�������";

    public static final int     portNoHotenavi           = 7023;
    public static final int     portNoHappyhotel         = 7046;
    public static final int     timeoutForHost           = 5000;

    /** �ŐV�t���O - �ŐV */
    public static final int     LATEST_FLAG_LATEST       = 1;
    /** �f�ڃt���O - �f�� */
    public static final int     PUBLISHING_FLAG_PUBLISH  = 1;

    /**
     * ���O�C�������[�敪
     * �n�b�s�z�e���F0
     */
    public static final int     LOGIN_KIND_HAPPYHOTEL    = 0;
    /**
     * ���O�C�������[�敪
     * �\�[�V�������O�C���F1
     */
    public static final int     LOGIN_KIND_SOCIALLOGIN   = 1;
    /**
     * ���O�C�������[�[��
     * �����A�v���F0
     */
    public static final int     LOGIN_TERMINAL_APP       = 0;
    /**
     * ���O�C�������[�[��
     * �X�}�z�v�d�a�F1
     */
    public static final int     LOGIN_TERMINAL_SP_WEB    = 1;
    /**
     * ���O�C�������[�[��
     * PC�v�d�a�F2
     */
    public static final int     LOGIN_TERMINAL_PC_WEB    = 2;
    /**
     * ���O�C�������[�[��
     * �\��A�v���F3
     */
    public static final int     LOGIN_TERMINAL_RSV_APP   = 3;
    /**
     * ���O�C�������[�[��
     * �����A�v���F4
     */
    public static final int     LOGIN_TERMINAL_SEL_APP   = 4;

    static
    {
        if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
        {
            configFilesPath = "C:\\ALMEX\\WWW\\WEB-INF\\";
        }
        else
        {
            configFilesPath = "//etc//happyhotel//";
        }
    }
    public static final int     MINIMUM_PASSWORD_LENGTH  = 8;
    public static final int     MAXIMUM_PASSWORD_LENGTH  = 32;
}
