package jp.happyhotel.dto;

import java.io.Serializable;

/**
 * ホテル顧客登録データ
 * 
 * @author
 * @version 1.00 2014/9/2
 */

public class DtoApHotelCustomerData implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String            customId;
    private String            userId;
    private String            password;
    private String            name;
    private String            furigana;
    private String            handleName;
    private int               sex;
    private int               birthday1Year;
    private int               birthday1Month;
    private int               birthday1Day;
    private int               birthday2Year;
    private int               birthday2Month;
    private int               birthday2Day;
    private int               memorial1Year;
    private int               memorial1Month;
    private int               memorial1Day;
    private int               memorial2Year;
    private int               memorial2Month;
    private int               memorial2Day;
    private int               prefCode1;
    private int               jisCode1;
    private int               prefCode2;
    private int               jisCode2;
    private String            tel1;
    private String            tel2;
    private String            mailAddr;
    private int               mailmag;
    private String            contents;
    private DtoApCommon       apCommon;             // タッチ共通データ

    // 1002電文で別途必要なデータ
    private int               birthMonth;
    private int               birthDay;
    private String            rank;
    private int               usage;
    private int               point;
    private int               point2;
    private int               birthDay1;
    private int               birthDay2;
    private int               birthDay1flag;
    private int               birthDay2flag;
    private int               memorial1;
    private int               memorial2;
    private int               memorial1flag;
    private int               memorial2flag;
    private int               discountRate;
    private String            carNoLocal;
    private String            carNoClass;
    private String            carNoType;
    private String            carNo;
    private int               mailStatus;
    private String            addr1;
    private String            addr2;
    private int               amount;
    private int               lastCome;
    private int               usageAll;
    private int               amountTotal;

    /**
     * データを初期化します。
     */
    public DtoApHotelCustomerData()
    {
        this.userId = "";
        this.customId = "";
        this.password = "";
        this.name = "";
        this.furigana = "";
        this.handleName = "";
        this.sex = 0;
        this.birthday1Year = 0;
        this.birthday1Month = 0;
        this.birthday1Day = 0;
        this.birthday2Year = 0;
        this.birthday2Month = 0;
        this.birthday2Day = 0;
        this.memorial1Year = 0;
        this.memorial1Month = 0;
        this.memorial1Day = 0;
        this.memorial2Year = 0;
        this.memorial2Month = 0;
        this.memorial2Day = 0;
        this.prefCode1 = 0;
        this.jisCode1 = 0;
        this.prefCode2 = 0;
        this.jisCode2 = 0;
        this.tel1 = "";
        this.tel2 = "";
        this.mailAddr = "";
        this.mailmag = 0;
        this.contents = "";
        this.apCommon = null;
        // 1002電文で追加になったデータ
        this.birthMonth = 0;
        this.birthDay = 0;
        this.rank = "";
        this.usage = 0;
        this.point = 0;
        this.point2 = 0;
        this.birthDay1 = 0;
        this.birthDay2 = 0;
        this.birthDay1flag = 0;
        this.birthDay2flag = 0;
        this.memorial1 = 0;
        this.memorial2 = 0;
        this.memorial1flag = 0;
        this.memorial2flag = 0;
        this.discountRate = 0;
        this.carNoLocal = "";
        this.carNoClass = "";
        this.carNoType = "";
        this.carNo = "";
        this.mailStatus = 0;
        this.addr1 = "";
        this.addr2 = "";
        this.tel1 = "";
        this.tel2 = "";
        this.amount = 0;
        this.lastCome = 0;
        this.usageAll = 0;
        this.amountTotal = 0;

    }

    public String getCustomId()
    {
        return customId;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getPassword()
    {
        return password;
    }

    public String getName()
    {
        return name;
    }

    public String getFurigana()
    {
        return furigana;
    }

    public String getHandleName()
    {
        return handleName;
    }

    public int getSex()
    {
        return sex;
    }

    public int getBirthday1Year()
    {
        return birthday1Year;
    }

    public int getBirthday1Month()
    {
        return birthday1Month;
    }

    public int getBirthday1Day()
    {
        return birthday1Day;
    }

    public int getBirthday2Year()
    {
        return birthday2Year;
    }

    public int getBirthday2Month()
    {
        return birthday2Month;
    }

    public int getBirthday2Day()
    {
        return birthday2Day;
    }

    public int getMemorial1Year()
    {
        return memorial1Year;
    }

    public int getMemorial1Month()
    {
        return memorial1Month;
    }

    public int getMemorial1Day()
    {
        return memorial1Day;
    }

    public int getMemorial2Year()
    {
        return memorial2Year;
    }

    public int getMemorial2Month()
    {
        return memorial2Month;
    }

    public int getMemorial2Day()
    {
        return memorial2Day;
    }

    public int getPrefCode1()
    {
        return prefCode1;
    }

    public int getJisCode1()
    {
        return jisCode1;
    }

    public int getPrefCode2()
    {
        return prefCode2;
    }

    public int getJisCode2()
    {
        return jisCode2;
    }

    public String getTel1()
    {
        return tel1;
    }

    public String getTel2()
    {
        return tel2;
    }

    public String getMailAddr()
    {
        return mailAddr;
    }

    public int getMailmag()
    {
        return mailmag;
    }

    public String getContents()
    {
        return contents;
    }

    public DtoApCommon getApCommon()
    {
        return apCommon;
    }

    public int getBirthMonth()
    {
        return birthMonth;
    }

    public int getBirthDay()
    {
        return birthDay;
    }

    public String getRank()
    {
        return rank;
    }

    public int getUsage()
    {
        return usage;
    }

    public int getPoint()
    {
        return point;
    }

    public int getPoint2()
    {
        return point2;
    }

    public int getBirthDay1()
    {
        return birthDay1;
    }

    public int getBirthDay2()
    {
        return birthDay2;
    }

    public int getBirthDay1flag()
    {
        return birthDay1flag;
    }

    public int getBirthDay2flag()
    {
        return birthDay2flag;
    }

    public int getMemorial1()
    {
        return memorial1;
    }

    public int getMemorial2()
    {
        return memorial2;
    }

    public int getMemorial1flag()
    {
        return memorial1flag;
    }

    public int getMemorial2flag()
    {
        return memorial2flag;
    }

    public int getDiscountRate()
    {
        return discountRate;
    }

    public String getCarNoLocal()
    {
        return carNoLocal;
    }

    public String getCarNoClass()
    {
        return carNoClass;
    }

    public String getCarNoType()
    {
        return carNoType;
    }

    public String getCarNo()
    {
        return carNo;
    }

    public int getMailStatus()
    {
        return mailStatus;
    }

    public String getAddr1()
    {
        return addr1;
    }

    public String getAddr2()
    {
        return addr2;
    }

    public int getAmount()
    {
        return amount;
    }

    public int getLastCome()
    {
        return lastCome;
    }

    public int getUsageAll()
    {
        return usageAll;
    }

    public int getAmountTotal()
    {
        return amountTotal;
    }

    public void setCustomId(String customId)
    {
        this.customId = customId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setFurigana(String furigana)
    {
        this.furigana = furigana;
    }

    public void setHandleName(String handleName)
    {
        this.handleName = handleName;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public void setBirthday1Year(int birthday1Year)
    {
        this.birthday1Year = birthday1Year;
    }

    public void setBirthday1Month(int birthday1Month)
    {
        this.birthday1Month = birthday1Month;
    }

    public void setBirthday1Day(int birthday1Day)
    {
        this.birthday1Day = birthday1Day;
    }

    public void setBirthday2Year(int birthday2Year)
    {
        this.birthday2Year = birthday2Year;
    }

    public void setBirthday2Month(int birthday2Month)
    {
        this.birthday2Month = birthday2Month;
    }

    public void setBirthday2Day(int birthday2Day)
    {
        this.birthday2Day = birthday2Day;
    }

    public void setMemorial1Year(int memorial1Year)
    {
        this.memorial1Year = memorial1Year;
    }

    public void setMemorial1Month(int memorial1Month)
    {
        this.memorial1Month = memorial1Month;
    }

    public void setMemorial1Day(int memorial1Day)
    {
        this.memorial1Day = memorial1Day;
    }

    public void setMemorial2Year(int memorial2Year)
    {
        this.memorial2Year = memorial2Year;
    }

    public void setMemorial2Month(int memorial2Month)
    {
        this.memorial2Month = memorial2Month;
    }

    public void setMemorial2Day(int memorial2Day)
    {
        this.memorial2Day = memorial2Day;
    }

    public void setPrefCode1(int prefCode1)
    {
        this.prefCode1 = prefCode1;
    }

    public void setJisCode1(int jisCode1)
    {
        this.jisCode1 = jisCode1;
    }

    public void setPrefCode2(int prefCode2)
    {
        this.prefCode2 = prefCode2;
    }

    public void setJisCode2(int jisCode2)
    {
        this.jisCode2 = jisCode2;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    public void setMailAddr(String mailAddr)
    {
        this.mailAddr = mailAddr;
    }

    public void setMailmag(int mailmag)
    {
        this.mailmag = mailmag;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public void setApCommon(DtoApCommon apCommon)
    {
        this.apCommon = apCommon;
    }

    public void setBirthMonth(int birthMonth)
    {
        this.birthMonth = birthMonth;
    }

    public void setBirthDay(int birthDay)
    {
        this.birthDay = birthDay;
    }

    public void setRank(String rank)
    {
        this.rank = rank;
    }

    public void setUsage(int usage)
    {
        this.usage = usage;
    }

    public void setPoint(int point)
    {
        this.point = point;
    }

    public void setPoint2(int point2)
    {
        this.point2 = point2;
    }

    public void setBirthDay1(int birthDay1)
    {
        this.birthDay1 = birthDay1;
    }

    public void setBirthDay2(int birthDay2)
    {
        this.birthDay2 = birthDay2;
    }

    public void setBirthDay1flag(int birthDay1flag)
    {
        this.birthDay1flag = birthDay1flag;
    }

    public void setBirthDay2flag(int birthDay2flag)
    {
        this.birthDay2flag = birthDay2flag;
    }

    public void setMemorial1(int memorial1)
    {
        this.memorial1 = memorial1;
    }

    public void setMemorial2(int memorial2)
    {
        this.memorial2 = memorial2;
    }

    public void setMemorial1flag(int memorial1flag)
    {
        this.memorial1flag = memorial1flag;
    }

    public void setMemorial2flag(int memorial2flag)
    {
        this.memorial2flag = memorial2flag;
    }

    public void setDiscountRate(int discountRate)
    {
        this.discountRate = discountRate;
    }

    public void setCarNoLocal(String carNoLocal)
    {
        this.carNoLocal = carNoLocal;
    }

    public void setCarNoClass(String carNoClass)
    {
        this.carNoClass = carNoClass;
    }

    public void setCarNoType(String carNoType)
    {
        this.carNoType = carNoType;
    }

    public void setCarNo(String carNo)
    {
        this.carNo = carNo;
    }

    public void setMailStatus(int mailStatus)
    {
        this.mailStatus = mailStatus;
    }

    public void setAddr1(String addr1)
    {
        this.addr1 = addr1;
    }

    public void setAddr2(String addr2)
    {
        this.addr2 = addr2;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void setLastCome(int lastCome)
    {
        this.lastCome = lastCome;
    }

    public void setUsageAll(int usageAll)
    {
        this.usageAll = usageAll;
    }

    public void setAmountTotal(int amountTotal)
    {
        this.amountTotal = amountTotal;
    }

}
