package jp.happyhotel.others;

/**
 * �O���T�[�r�X�A�g�p���񋟃C���^�[�t�F�[�X<br>
 * <br>
 * Yahoo!��i�r�^�C���Ȃǂɒ񋟂�����𒊏o���邽�߂̋@�\�������C���^�[�t�F�[�X�ł��B<br>
 * 
 * @author koshiba-y1
 */
public interface InformationProvider
{
    /**
     * �f�[�^���o�B
     */
    void select();

    /**
     * �f�[�^�o�́B
     * 
     * @return �O���񋟏��
     */
    String export();
}
