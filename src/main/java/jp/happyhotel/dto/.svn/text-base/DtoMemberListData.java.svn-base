package jp.happyhotel.dto;

public class DtoMemberListData
{
    String         header;

    // 送信電文
    private int    seq;
    private int    userId;    // ハピホテ側ホテルごとのユーザID
    private int    registDate; // 移行日付
    private String memberId;  // ホスト側メンバーID
    private int    kind;      // 移行状況　1:カードレス移行済(磁気カード+カードレス)　2:カードレス新規(カードレスのみ)
    private String reserve;   // 5バイト
    private int    identifyNo; // 識別コード method=RsvListのパラメータより

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
