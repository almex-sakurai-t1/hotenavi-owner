package jp.happyhotel.dto;

import java.io.Serializable;

/**
 * �����o�[�J�[�h�o�^
 * 
 * @author Shingo Tashiro
 * @version 1.00 2014/8/17
 */
public class DtoApMemberCardReg implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 3158523505508145540L;

    private boolean           result;                                 // ������������
    private boolean           resultMemberCard;                       // �����o�[��t��񌋉�
    private boolean           cardRegFlag;                            // ����J�[�h��t�ς݂�
    private String            customId;                               // �z�e������ԍ�
    // private String handleName; // �o�^���j�b�N�l�[��
    // private int birthday1_year; // �a����1(�N)
    // private int birthday1_month; // �a����1(��)
    // private int birthday1_day; // �a����1(��)
    // private int birthday2_year; // �a����2(�N)
    // private int birthday2_month; // �a����2(��)
    // private int birthday2_day; // �a����2(��)
    // private int memorialday1_year; // �L�O��1(�N)
    // private int memorialday1_month; // �L�O��1(��)
    // private int memorialday1_day; // �L�O��1(��)
    // private int memorialday2_year; // �L�O��2(�N)
    // private int memorialday2_month; // �L�O��2(��)
    // private int memorialday2_day; // �L�O��2(��)
    private boolean           hotenaviContract;                       // �z�e�i�r�_��
    private int               errorCode;                              // �G���[�R�[�h
    private String            errorMessage;                           // �G���[���b�Z�[�W
    private boolean           duplicationRegFlag;                     // �d������o�^�t���O
    private DtoApCommon       apCommon;                               // �^�b�`���ʃf�[�^
    private int               goodsCode;                              // ���i�R�[�h
    private int               goodsPrice;                             // ����

    /**
     * �f�[�^�����������܂��B
     */
    public DtoApMemberCardReg()
    {
        this.result = false;
        this.resultMemberCard = false;
        this.cardRegFlag = false;
        this.customId = "";
        // this.handleName = "";
        // this.birthday1_year = 0;
        // this.birthday1_month = 0;
        // this.birthday1_day = 0;
        // this.birthday2_year = 0;
        // this.birthday2_month = 0;
        // this.birthday2_day = 0;
        // this.memorialday1_year = 0;
        // this.memorialday1_month = 0;
        // this.memorialday1_day = 0;
        // this.memorialday2_year = 0;
        // this.memorialday2_month = 0;
        // this.memorialday2_day = 0;
        this.hotenaviContract = false;
        this.errorCode = 0;
        this.errorMessage = "";
        this.duplicationRegFlag = false;
        this.apCommon = null;
        this.goodsCode = 0;
        this.goodsPrice = 0;
    }

    public boolean isResult()
    {
        return result;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public boolean isResultMemberCard()
    {
        return resultMemberCard;
    }

    public void setResultMemberCard(boolean resultMemberCard)
    {
        this.resultMemberCard = resultMemberCard;
    }

    public boolean isCardRegFlag()
    {
        return cardRegFlag;
    }

    public void setCardRegFlag(boolean cardRegFlag)
    {
        this.cardRegFlag = cardRegFlag;
    }

    public String getCustomId()
    {
        return customId;
    }

    public void setCustomId(String customId)
    {
        this.customId = customId;
    }

    //
    // public String getHandleName()
    // {
    // return handleName;
    // }
    //
    // public void setHandleName(String handleName)
    // {
    // this.handleName = handleName;
    // }

    public boolean isHotenaviContract()
    {
        return hotenaviContract;
    }

    public void setHotenaviContract(boolean hotenaviContract)
    {
        this.hotenaviContract = hotenaviContract;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public boolean isDuplicationRegFlag()
    {
        return duplicationRegFlag;
    }

    public void setDuplicationRegFlag(boolean duplicationRegFlag)
    {
        this.duplicationRegFlag = duplicationRegFlag;
    }

    public DtoApCommon getApCommon()
    {
        return apCommon;
    }

    public void setApCommon(DtoApCommon apCommon)
    {
        this.apCommon = apCommon;
    }

    public int getGoodsCode()
    {
        return goodsCode;
    }

    public void setGoodsCode(int goodsCode)
    {
        this.goodsCode = goodsCode;
    }

    public int getGoodsPrice()
    {
        return goodsPrice;
    }

    public void setGoodsPrice(int goodsPrice)
    {
        this.goodsPrice = goodsPrice;
    }

    // public int getBirthday1_year() {
    // return birthday1_year;
    // }
    //
    // public void setBirthday1_year(int birthday1_year) {
    // this.birthday1_year = birthday1_year;
    // }
    //
    // public int getBirthday1_month() {
    // return birthday1_month;
    // }
    //
    // public void setBirthday1_month(int birthday1_month) {
    // this.birthday1_month = birthday1_month;
    // }
    //
    // public int getBirthday1_day() {
    // return birthday1_day;
    // }
    //
    // public void setBirthday1_day(int birthday1_day) {
    // this.birthday1_day = birthday1_day;
    // }
    //
    // public int getBirthday2_year() {
    // return birthday2_year;
    // }
    //
    // public void setBirthday2_year(int birthday2_year) {
    // this.birthday2_year = birthday2_year;
    // }
    //
    // public int getBirthday2_month() {
    // return birthday2_month;
    // }
    //
    // public void setBirthday2_month(int birthday2_month) {
    // this.birthday2_month = birthday2_month;
    // }
    //
    // public int getBirthday2_day() {
    // return birthday2_day;
    // }
    //
    // public void setBirthday2_day(int birthday2_day) {
    // this.birthday2_day = birthday2_day;
    // }
    //
    // public int getMemorialday1_year() {
    // return memorialday1_year;
    // }
    //
    // public void setMemorialday1_year(int memorialday1_year) {
    // this.memorialday1_year = memorialday1_year;
    // }
    //
    // public int getMemorialday1_month() {
    // return memorialday1_month;
    // }
    //
    // public void setMemorialday1_month(int memorialday1_month) {
    // this.memorialday1_month = memorialday1_month;
    // }
    //
    // public int getMemorialday1_day() {
    // return memorialday1_day;
    // }
    //
    // public void setMemorialday1_day(int memorialday1_day) {
    // this.memorialday1_day = memorialday1_day;
    // }
    //
    // public int getMemorialday2_year() {
    // return memorialday2_year;
    // }
    //
    // public void setMemorialday2_year(int memorialday2_year) {
    // this.memorialday2_year = memorialday2_year;
    // }
    //
    // public int getMemorialday2_month() {
    // return memorialday2_month;
    // }
    //
    // public void setMemorialday2_month(int memorialday2_month) {
    // this.memorialday2_month = memorialday2_month;
    // }
    //
    // public int getMemorialday2_day() {
    // return memorialday2_day;
    // }
    //
    // public void setMemorialday2_day(int memorialday2_day) {
    // this.memorialday2_day = memorialday2_day;
    // }

}
