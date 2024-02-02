package jp.happyhotel.common;

public class ConstantsMemberReg
{

    // ������
    public static final int  USERID_ABOVE        = 1;
    public static final int  USERID_FOLLOW       = 32;
    public static final int  PASSWORD_ABOVE      = 4;
    public static final int  PASSWORD_FOLLOW     = 8;
    public static final int  PASSWORD_ABOVE_SC   = 8;
    public static final int  PASSWORD_FOLLOW_SC  = 16;
    public static final int  NAME_ABOVE          = 1;
    public static final int  NAME_FOLLOW         = 40;
    public static final int  FURIGANA_ABOVE      = 1;
    public static final int  FURIGANA_FOLLOW     = 20;
    public static final int  HANDLENAME_ABOVE    = 1;
    public static final int  HANDLENAME_FOLLOW   = 40;
    // public static final int ADDRESS_ABOVE = 1;
    // public static final int ADDRESS_FOLLOW = 2;
    public static final int  TELLNUMBER_ABOVE    = 1;
    public static final int  TELLNUMBER_FOLLOW   = 15;
    public static final int  MAIL_ADDRESS_ABOVE  = 1;
    public static final int  MAIL_ADDRESS_FOLLOW = 63;

    // ���͍���
    public static final int  USER_ID             = 0; // ���[�U�[ID
    public static final int  PASSWORD            = 1; // �p�X���[�h
    public static final int  PASSWORD_COFIRM     = 2; // �p�X���[�h(�m�F�p)
    public static final int  NAME                = 3; // ���O
    public static final int  FURIGANA            = 4; // �t���K�i
    public static final int  SEX                 = 5; // ����
    public static final int  HANDLE_NAME         = 6; // �j�b�N�l�[��
    public static final int  BIRTHDAY1_YEAR      = 7; // �a����1(�N)
    public static final int  BIRTHDAY1_MONTH     = 8; // �a����1(��)
    public static final int  BIRTHDAY1_DAY       = 9; // �a����1(��)
    public static final int  BIRTHDAY2_YEAR      = 10; // �a����2(�N)
    public static final int  BIRTHDAY2_MONTH     = 11; // �a����2(��)
    public static final int  BIRTHDAY2_DAY       = 12; // �a����2(��)
    public static final int  MEMORIALDAY1_YEAR   = 13; // �L�O��1(�N)
    public static final int  MEMORIALDAY1_MONTH  = 14; // �L�O��1(��)
    public static final int  MEMORIALDAY1_DAY    = 15; // �L�O��1(��)
    public static final int  MEMORIALDAY2_YEAR   = 16; // �L�O��2(�N)
    public static final int  MEMORIALDAY2_MONTH  = 17; // �L�O��2(��)
    public static final int  MEMORIALDAY2_DAY    = 18; // �L�O��2(��)
    public static final int  PREF_CODE1          = 19; // �s���{��1
    public static final int  JISCODE1            = 20; // �s�撬��1
    public static final int  PREF_CODE2          = 21; // �s���{��2
    public static final int  JISCODE2            = 22; // �s�撬��2
    public static final int  TELL_NUMBER1        = 23; // �d�b�ԍ�1
    public static final int  TELL_NUMBER2        = 24; // �d�b�ԍ�2
    public static final int  MAIL_ADDRESS        = 25; // ���[���A�h���X
    public static final int  MAIL_ADDRESS_COFIRM = 26; // ���[���A�h���X(�m�F�p)
    public static final int  MAIL_MAGAZINE       = 27; // �����}�K

    // error����
    public static final int  UNINPUTTED_ERR      = 0; // ������
    public static final int  SPACE_ERR           = 1; // �󔒂̂�
    public static final int  STRING_TYPE__ERR    = 2; // �S���p�`�F�b�N
    public static final int  NUM_OF_CHARA_ERR    = 3; // ���͕���������
    public static final int  ALREADY_USED_ERR    = 4; // ���łɓo�^��
    public static final int  REGIST_ERR          = 5; // �o�^�s��
    public static final int  COMPARE_ERR         = 6; // (�m�F�p)�Ƃ̑���
    public static final int  MAIL_FORMAT_ERR     = 7; // ���[���A�h���X�t�H�[�}�b�g
    public static final int  MAIL_INTERIM_REG_   = 8; // ���o�^����
    public static final int  MAIL_REG_COMPLETED  = 9; // �o�^����
    public static final int  NUM_OF_CHARA_ERR_SC = 10; // ���͕���������(�X�e�C�R���V�F���W��)

    public static String[][] errorMessageTable;

    static
    {
        errorMessageTable = new String[30][11];
        errorMessageTable[USER_ID][UNINPUTTED_ERR] = "�u���[�U�[ID�v�����͂���Ă��܂���B";
        errorMessageTable[USER_ID][STRING_TYPE__ERR] = "�u���[�U�[ID�v�͔��p�p���œ��͂��Ă��������B�i�����݂̂�ID�͓o�^�ł��܂���B�j";
        errorMessageTable[USER_ID][NUM_OF_CHARA_ERR] = "�u���[�U�[ID�v��4�`20�����œ��͂��Ă��������B";
        errorMessageTable[USER_ID][ALREADY_USED_ERR] = "���͂����u���[�U�[ID�v�͂��łɎg�p����Ă��܂��B";
        errorMessageTable[USER_ID][REGIST_ERR] = "���͂����u���[�U�[ID�v�͓o�^�ł��܂���B";

        errorMessageTable[PASSWORD][UNINPUTTED_ERR] = "�u�p�X���[�h�v�����͂���Ă��܂���B";
        errorMessageTable[PASSWORD][STRING_TYPE__ERR] = "�u�p�X���[�h�v�́A���p�p�����œ��͂��Ă��������B";
        errorMessageTable[PASSWORD][NUM_OF_CHARA_ERR] = "�u�p�X���[�h�v�́A4�`8�����œ��͂��Ă��������B";
        errorMessageTable[PASSWORD][REGIST_ERR] = "�u���[�U�[ID�v�Ɓu�p�X���[�h�v�͈Ⴄ���̂���͂��Ă��������B";
        errorMessageTable[PASSWORD][COMPARE_ERR] = "�u�p�X���[�h�v�Ɓu�p�X���[�h�i�m�F�p�j�v������Ă��܂��B";
        errorMessageTable[PASSWORD][NUM_OF_CHARA_ERR_SC] = "�u�p�X���[�h�v�́A8�`16�����œ��͂��Ă��������B";

        errorMessageTable[NAME][UNINPUTTED_ERR] = "�u���O�v�����͂���Ă��܂���B";
        errorMessageTable[NAME][SPACE_ERR] = "�󔒂݂̂́u���O�v�͓o�^�ł��܂���B";
        errorMessageTable[NAME][STRING_TYPE__ERR] = "�u���O�v�͑S�p�œ��͂��Ă��������B";
        errorMessageTable[NAME][NUM_OF_CHARA_ERR] = "�u���O�v��1�`20�����œ��͂��Ă��������B";

        errorMessageTable[FURIGANA][UNINPUTTED_ERR] = "�u�t���K�i�v�����͂���Ă��܂���B";
        errorMessageTable[FURIGANA][STRING_TYPE__ERR] = "�u�t���K�i�v�͑S���p�J�i�œ��͂��Ă��������B";
        errorMessageTable[FURIGANA][NUM_OF_CHARA_ERR] = "�u�t���K�i�v��1�`20�����œ��͂��Ă��������B";

        errorMessageTable[SEX][UNINPUTTED_ERR] = "�u���ʁv���I������Ă��܂���B";

        errorMessageTable[HANDLE_NAME][UNINPUTTED_ERR] = "�u�j�b�N�l�[���v�����͂���Ă��܂���B";
        errorMessageTable[HANDLE_NAME][SPACE_ERR] = "�󔒂݂̂́u�j�b�N�l�[���v�͓o�^�ł��܂���B";
        errorMessageTable[HANDLE_NAME][STRING_TYPE__ERR] = "�u�j�b�N�l�[���v�͑S�p�œ��͂��Ă��������B";
        errorMessageTable[HANDLE_NAME][NUM_OF_CHARA_ERR] = "�u�j�b�N�l�[���v��1�`20�����œ��͂��Ă��������B";
        errorMessageTable[HANDLE_NAME][ALREADY_USED_ERR] = "���͂����u�j�b�N�l�[���v�͂��łɎg�p����Ă��܂��B";
        errorMessageTable[HANDLE_NAME][REGIST_ERR] = "���͂����u�j�b�N�l�[���v�͓o�^�ł��܂���B";

        errorMessageTable[BIRTHDAY1_YEAR][UNINPUTTED_ERR] = "�u���N����1�i�N�j�v���I������Ă��܂���B";
        errorMessageTable[BIRTHDAY1_MONTH][UNINPUTTED_ERR] = "�u���N����1�i���j�v���I������Ă��܂���B";
        errorMessageTable[BIRTHDAY1_DAY][UNINPUTTED_ERR] = "�u���N����1�i���j�v���I������Ă��܂���B";
        errorMessageTable[BIRTHDAY1_DAY][REGIST_ERR] = "�������u���N����1�i���j�v��I�����Ă��������B";

        errorMessageTable[BIRTHDAY2_YEAR][UNINPUTTED_ERR] = "�u���N����2�i�N�j�v���I������Ă��܂���B";
        errorMessageTable[BIRTHDAY2_MONTH][UNINPUTTED_ERR] = "�u���N����2�i���j�v���I������Ă��܂���B";
        errorMessageTable[BIRTHDAY2_DAY][UNINPUTTED_ERR] = "�u���N����2�i���j�v���I������Ă��܂���B";
        errorMessageTable[BIRTHDAY2_DAY][REGIST_ERR] = "�������u���N����2�i���j�v��I�����Ă��������B";

        errorMessageTable[MEMORIALDAY1_YEAR][UNINPUTTED_ERR] = "�u�L�O��1�i�N�j�v���I������Ă��܂���B";
        errorMessageTable[MEMORIALDAY1_MONTH][UNINPUTTED_ERR] = "�u�L�O��1�i���j�v���I������Ă��܂���B";
        errorMessageTable[MEMORIALDAY1_DAY][UNINPUTTED_ERR] = "�u�L�O��1�i���j�v���I������Ă��܂���B";
        errorMessageTable[MEMORIALDAY1_DAY][REGIST_ERR] = "�������u�L�O��1�i���j�v��I�����Ă��������B";

        errorMessageTable[MEMORIALDAY2_YEAR][UNINPUTTED_ERR] = "�u�L�O��2�i�N�j�v���I������Ă��܂���B";
        errorMessageTable[MEMORIALDAY2_MONTH][UNINPUTTED_ERR] = "�u�L�O��2�i���j�v���I������Ă��܂���B";
        errorMessageTable[MEMORIALDAY2_DAY][UNINPUTTED_ERR] = "�u�L�O��2�i���j�v���I������Ă��܂���B";
        errorMessageTable[MEMORIALDAY2_DAY][REGIST_ERR] = "�������u�L�O��2�i���j�v��I�����Ă��������B";

        errorMessageTable[PREF_CODE1][UNINPUTTED_ERR] = "�u���Z�܂��̓s���{��1�v���I������Ă��܂���B";
        errorMessageTable[JISCODE1][UNINPUTTED_ERR] = "�u���Z�܂��̎s�撬��1�v���I������Ă��܂���B";
        errorMessageTable[PREF_CODE2][UNINPUTTED_ERR] = "�u���Z�܂��̓s���{��2�v���I������Ă��܂���B";
        errorMessageTable[JISCODE2][UNINPUTTED_ERR] = "�u���Z�܂��̎s�撬��2�v���I������Ă��܂���B";

        errorMessageTable[TELL_NUMBER1][UNINPUTTED_ERR] = "�u�d�b�ԍ�1�v�����͂���Ă��܂���B";
        errorMessageTable[TELL_NUMBER1][STRING_TYPE__ERR] = "�u�d�b�ԍ�1�v�͔��p�����œ��͂��Ă��������B";
        errorMessageTable[TELL_NUMBER1][NUM_OF_CHARA_ERR] = "�u�d�b�ԍ�1�v��15�����œ��͂��Ă��������B";
        errorMessageTable[TELL_NUMBER2][UNINPUTTED_ERR] = "�u�d�b�ԍ�2�v�����͂���Ă��܂���B";
        errorMessageTable[TELL_NUMBER2][STRING_TYPE__ERR] = "�u�d�b�ԍ�2�v�͔��p�����œ��͂��Ă��������B";
        errorMessageTable[TELL_NUMBER2][NUM_OF_CHARA_ERR] = "�u�d�b�ԍ�2�v��15�����œ��͂��Ă��������B";

        errorMessageTable[MAIL_ADDRESS][UNINPUTTED_ERR] = "�u���[���A�h���X�v�����͂���Ă��܂���B";
        errorMessageTable[MAIL_ADDRESS][MAIL_FORMAT_ERR] = "�u���[���A�h���X�v�𐳂������͂��Ă��������B";
        errorMessageTable[MAIL_ADDRESS][REGIST_ERR] = "���͂����u���[���A�h���X�v�͓o�^�ł��܂���B";
        errorMessageTable[MAIL_ADDRESS][COMPARE_ERR] = "�u���[���A�h���X�v�Ɓu���[���A�h���X(�m�F�p)�v������Ă��܂��B";
        // errorMessageTable[MAIL_ADDRESS_COFIRM][UNINPUTTED_ERR] = "�u���[���A�h���X(�m�F�p)�v�����͂���Ă��܂���B";
        errorMessageTable[MAIL_ADDRESS][MAIL_INTERIM_REG_] = "���͂����u���[���A�h���X�v�ŉ��o�^���������Ă��܂��B���o�^�̃��[���A�h���X���ɂ��m�F�̃��[���������肵�Ă���܂��̂ŁAURL���N���b�N������o�^���������Ă��������B���m�F�̃��[�����͂��Ă��Ȃ��ꍇ�́A�����p�̃��[���A�h���X���ēx���m�F���������B";
        errorMessageTable[MAIL_ADDRESS][MAIL_REG_COMPLETED] = "���͂����u���[���A�h���X�v�œo�^���������Ă��܂��B";

        errorMessageTable[MAIL_MAGAZINE][UNINPUTTED_ERR] = "�u�����}�K�o�^�v���I������Ă��܂���B";
    }
}
