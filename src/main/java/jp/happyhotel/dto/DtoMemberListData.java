package jp.happyhotel.dto;

public class DtoMemberListData
{
    String         header;

    // ���M�d��
    private int    seq;
    private int    userId;    // �n�s�z�e���z�e�����Ƃ̃��[�UID
    private int    registDate; // �ڍs���t
    private String memberId;  // �z�X�g�������o�[ID
    private int    kind;      // �ڍs�󋵁@1:�J�[�h���X�ڍs��(���C�J�[�h+�J�[�h���X)�@2:�J�[�h���X�V�K(�J�[�h���X�̂�)
    private String reserve;   // 5�o�C�g
    private int    identifyNo; // ���ʃR�[�h method=RsvList�̃p�����[�^���

    public DtoMemberListData()
    {
        seq = 0;
        userId = 0;
        registDate = 0;
        memberId = "";
        kind = 0;
        identifyNo = 0;

        reserve = "";
    }

    public int getSeq()
    {
        return seq;
    }

    public int getUserId()
    {
        return userId;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getKind()
    {
        return kind;
    }

    public String getMemberId()
    {
        return memberId;
    }

    public String getReserve()
    {
        return reserve;
    }

    public int getIdentifyNo()
    {
        return identifyNo;
    }

    public void setSeq(int seq)
    {
        this.seq = seq;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setMemberId(String memberId)
    {
        this.memberId = memberId;
    }

    public void setReserve(String reserve)
    {
        this.reserve = reserve;
    }

    public void setIdentifyNo(int identifyNo)
    {
        this.identifyNo = identifyNo;
    }
}
