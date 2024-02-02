package jp.happyhotel.owner;

/*
 * 来店明細Formクラス
 */
public class FormOwnerBkoComingDetails
{
    //
    private int     selHotelID       = 0;
    private String  selHotenaviID    = "";
    private String  selHotelName     = "";
    private String  hotenaviID       = "";
    private int     userId           = 0;
    private int     imediaFlg        = 0;    // 事務局
    private int     billOwnFlg       = 0;    // 請求閲覧可能担当者
    private int     closingKind      = 0;    // 締め処理区分
    private int     modeFlg          = 0;    // 呼び出し元画面ID
    private int     sonotaFlg        = 0;    // 商品コードが200番台の明細かを判断する（0：その対外 1：その他）
    private boolean existsSeikyu     = false; // 請求・売掛データが存在するか

    private int     billSlipNo       = 0;    // 請求伝票No
    private int     accrecvSlipNo    = 0;    // 売掛伝票No
    private int     newAccSLipNo     = 0;    // 新しい売掛伝票No.
    private String  usageDate        = "";   // 利用日付
    private String  slipUpdate       = "";   // 更新日付
    private String  personName       = "";   // 担当者
    private int     usrMngNo         = 0;    // ユーザ管理番号
    private String  htSlipNo         = "";   // 伝票No.
    private String  htRoomNo         = "";   // 部屋
    private String  usageCharge      = "";   // 利用金額
    private String  usageChargeStr   = "";
    private double  bairitu          = 0;    // 付与倍率
    private String  receiveCharge    = "";   // 予約金額
    private String  receiveChargeStr = "";
    private int     hRaiten          = 0;    // ハッピー発生　来店
    private int     hSeisan          = 0;    // ハッピー発生　精算 計算用
    private String  hSeisan_View     = "";   // ハッピー発生　精算 表示用
    private String  hSeisan_Inp      = "";   // ハッピー発生　精算 入力用
    private int     hWaribiki        = 0;    // ハッピー発生　割引 計算用
    private String  hWaribiki_View   = "";   // ハッピー発生　割引 表示用
    private String  hWaribiki_Inp    = "";   // ハッピー発生　割引 入力用
    private int     hYoyaku          = 0;    // ハッピー発生　予約
    private int     hBonusMile       = 0;    // 予約ボーナスマイル
    private String  hSum             = "";   // ハッピー発生　合計
    private int     hZandaka         = 0;    // ハッピー残高　現在
    private int     sRaiten          = 0;    // 請求・支払　来店
    private String  sSeisan          = "";   // 請求・支払　精算
    private String  sWaribiki        = "";   // 請求・支払　割引
    private int     sWaribikiInt     = 0;
    private String  sYoyaku          = "";   // 請求・支払　予約
    private String  sBonusMile       = "";   // 予約ボーナスマイル
    private String  sSum             = "";   // 請求・支払　合計
    private String  sonota1          = "";   // その他支払1
    private String  sonota1Charge    = "";
    private String  sonota2          = "";   // その他請求・支払2
    private String  sonota2Charget   = "";
    private String  errMsg           = "";   //

    // 来店追加
    private String  selYear          = "";   // 利用年
    private String  selMonth         = "";   // 利用月
    private String  selDate          = "";   // 利用日

    private int     inpUsageDate     = 0;    // 利用年月日
    private String  inpPersonNm      = "";   // 担当者名
    private String  inpHtRoomNo      = "";   // 部屋
    private String  inpUsageCharge   = "";   // 利用金額
    private String  inpRsvCharge     = "";   // 予約金額
    private String  inpWaribiki      = "";   // 割引

    private String  selYearFrom      = "";
    private String  selMonthFrom     = "";
    private String  selDateFrom      = "";
    private String  selYearTo        = "";
    private String  selMonthTo       = "";
    private String  selDateTo        = "";
    private int     page             = 0;

    private int     callPage         = 0;    // 呼び出し元ページ(1:ハピホテマイル履歴、2:収支明細)

    /**
     * selHotelIDを取得します。
     * 
     * @return selHotelID
     */
    public int getSelHotelID()
    {
        return selHotelID;
    }

    /**
     * selHotelIDを設定します。
     * 
     * @param selHotelID selHotelID
     */
    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    /**
     * selHotenaviIDを取得します。
     * 
     * @return selHotenaviID
     */
    public String getSelHotenaviID()
    {
        return selHotenaviID;
    }

    /**
     * selHotenaviIDを設定します。
     * 
     * @param selHotenaviID selHotenaviID
     */
    public void setSelHotenaviID(String selHotenaviID)
    {
        this.selHotenaviID = selHotenaviID;
    }

    /**
     * selHotelNameを取得します。
     * 
     * @return selHotelName
     */
    public String getSelHotelName()
    {
        return selHotelName;
    }

    /**
     * selHotelNameを設定します。
     * 
     * @param selHotelName selHotelName
     */
    public void setSelHotelName(String selHotelName)
    {
        this.selHotelName = selHotelName;
    }

    /**
     * accrecvSlipNoを取得します。
     * 
     * @return accrecvSlipNo
     */
    public int getAccrecvSlipNo()
    {
        return accrecvSlipNo;
    }

    /**
     * accrecvSlipNoを設定します。
     * 
     * @param accrecvSlipNo accrecvSlipNo
     */
    public void setAccrecvSlipNo(int accrecvSlipNo)
    {
        this.accrecvSlipNo = accrecvSlipNo;
    }

    /**
     * usageDateを取得します。
     * 
     * @return usageDate
     */
    public String getUsageDate()
    {
        return usageDate;
    }

    /**
     * usageDateを設定します。
     * 
     * @param usageDate usageDate
     */
    public void setUsageDate(String usageDate)
    {
        this.usageDate = usageDate;
    }

    /**
     * slipUpdateを取得します。
     * 
     * @return slipUpdate
     */
    public String getSlipUpdate()
    {
        return slipUpdate;
    }

    /**
     * slipUpdateを設定します。
     * 
     * @param slipUpdate slipUpdate
     */
    public void setSlipUpdate(String slipUpdate)
    {
        this.slipUpdate = slipUpdate;
    }

    /**
     * personNameを取得します。
     * 
     * @return personName
     */
    public String getPersonName()
    {
        return personName;
    }

    /**
     * personNameを設定します。
     * 
     * @param personName personName
     */
    public void setPersonName(String personName)
    {
        this.personName = personName;
    }

    /**
     * htSlipNoを取得します。
     * 
     * @return htSlipNo
     */
    public String getHtSlipNo()
    {
        return htSlipNo;
    }

    /**
     * htSlipNoを設定します。
     * 
     * @param htSlipNo htSlipNo
     */
    public void setHtSlipNo(String htSlipNo)
    {
        this.htSlipNo = htSlipNo;
    }

    /**
     * htRoomNoを取得します。
     * 
     * @return htRoomNo
     */
    public String getHtRoomNo()
    {
        return htRoomNo;
    }

    /**
     * htRoomNoを設定します。
     * 
     * @param htRoomNo htRoomNo
     */
    public void setHtRoomNo(String htRoomNo)
    {
        this.htRoomNo = htRoomNo;
    }

    /**
     * usageChargeを取得します。
     * 
     * @return usageCharge
     */
    public String getUsageCharge()
    {
        return usageCharge;
    }

    /**
     * usageChargeを設定します。
     * 
     * @param usageCharge usageCharge
     */
    public void setUsageCharge(String usageCharge)
    {
        this.usageCharge = usageCharge;
    }

    /**
     * receiveChargeを取得します。
     * 
     * @return receiveCharge
     */
    public String getReceiveCharge()
    {
        return receiveCharge;
    }

    /**
     * receiveChargeを設定します。
     * 
     * @param receiveCharge receiveCharge
     */
    public void setReceiveCharge(String receiveCharge)
    {
        this.receiveCharge = receiveCharge;
    }

    /**
     * hRaitenを取得します。
     * 
     * @return hRaiten
     */
    public int gethRaiten()
    {
        return hRaiten;
    }

    /**
     * hRaitenを設定します。
     * 
     * @param hRaiten hRaiten
     */
    public void sethRaiten(int hRaiten)
    {
        this.hRaiten = hRaiten;
    }

    /**
     * hSeisanを取得します。
     * 
     * @return hSeisan
     */
    public int gethSeisan()
    {
        return hSeisan;
    }

    /**
     * hSeisanを設定します。
     * 
     * @param hSeisan hSeisan
     */
    public void sethSeisan(int hSeisan)
    {
        this.hSeisan = hSeisan;
    }

    /**
     * hYoyakuを取得します。
     * 
     * @return hYoyaku
     */
    public int gethYoyaku()
    {
        return hYoyaku;
    }

    /**
     * hYoyakuを設定します。
     * 
     * @param hYoyaku hYoyaku
     */
    public void sethYoyaku(int hYoyaku)
    {
        this.hYoyaku = hYoyaku;
    }

    /**
     * hBonusMileを取得します。
     * 
     * @return hBonusMile
     */
    public int gethBonusMile()
    {
        return hBonusMile;
    }

    /**
     * hBonusMileを設定します。
     * 
     * @param hBonusMile hBonusMile
     */
    public void sethBonusMile(int hBonusMile)
    {
        this.hBonusMile = hBonusMile;
    }

    /**
     * hSumを取得します。
     * 
     * @return hSum
     */
    public String gethSum()
    {
        return hSum;
    }

    /**
     * hSumを設定します。
     * 
     * @param hSum hSum
     */
    public void sethSum(String hSum)
    {
        this.hSum = hSum;
    }

    /**
     * hZandakaを取得します。
     * 
     * @return hZandaka
     */
    public int gethZandaka()
    {
        return hZandaka;
    }

    /**
     * hZandakaを設定します。
     * 
     * @param hZandaka hZandaka
     */
    public void sethZandaka(int hZandaka)
    {
        this.hZandaka = hZandaka;
    }

    /**
     * sRaitenを取得します。
     * 
     * @return sRaiten
     */
    public int getsRaiten()
    {
        return sRaiten;
    }

    /**
     * sRaitenを設定します。
     * 
     * @param sRaiten sRaiten
     */
    public void setsRaiten(int sRaiten)
    {
        this.sRaiten = sRaiten;
    }

    /**
     * sSeisanを取得します。
     * 
     * @return sSeisan
     */
    public String getsSeisan()
    {
        return sSeisan;
    }

    /**
     * sSeisanを設定します。
     * 
     * @param sSeisan sSeisan
     */
    public void setsSeisan(String sSeisan)
    {
        this.sSeisan = sSeisan;
    }

    /**
     * sWaribikiを取得します。
     * 
     * @return sWaribiki
     */
    public String getsWaribiki()
    {
        return sWaribiki;
    }

    /**
     * sWaribikiを設定します。
     * 
     * @param sWaribiki sWaribiki
     */
    public void setsWaribiki(String sWaribiki)
    {
        this.sWaribiki = sWaribiki;
    }

    /**
     * sYoyakuを取得します。
     * 
     * @return sYoyaku
     */
    public String getsYoyaku()
    {
        return sYoyaku;
    }

    /**
     * sYoyakuを設定します。
     * 
     * @param sYoyaku sYoyaku
     */
    public void setsYoyaku(String sYoyaku)
    {
        this.sYoyaku = sYoyaku;
    }

    /**
     * sBonusMileを取得します。
     * 
     * @return sBonusMile
     */
    public String getsBonusMile()
    {
        return sBonusMile;
    }

    /**
     * sBonusMileを設定します。
     * 
     * @param sBonusMile sBonusMile
     */
    public void setsBonusMile(String sBonusMile)
    {
        this.sBonusMile = sBonusMile;
    }

    /**
     * sSumを取得します。
     * 
     * @return sSum
     */
    public String getsSum()
    {
        return sSum;
    }

    /**
     * sSumを設定します。
     * 
     * @param sSum sSum
     */
    public void setsSum(String sSum)
    {
        this.sSum = sSum;
    }

    /**
     * errMsgを取得します。
     * 
     * @return errMsg
     */
    public String getErrMsg()
    {
        return errMsg;
    }

    /**
     * errMsgを設定します。
     * 
     * @param errMsg errMsg
     */
    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public int getUsrMngNo()
    {
        return usrMngNo;
    }

    public void setUsrMngNo(int usrMngNo)
    {
        this.usrMngNo = usrMngNo;
    }

    public String getUsageChargeStr()
    {
        return usageChargeStr;
    }

    public void setUsageChargeStr(String usageChargeStr)
    {
        this.usageChargeStr = usageChargeStr;
    }

    public String getReceiveChargeStr()
    {
        return receiveChargeStr;
    }

    public void setReceiveChargeStr(String receiveChargeStr)
    {
        this.receiveChargeStr = receiveChargeStr;
    }

    public String gethWaribiki_View()
    {
        return hWaribiki_View;
    }

    public void sethWaribiki_View(String hWaribiki_View)
    {
        this.hWaribiki_View = hWaribiki_View;
    }

    public void sethWaribiki(int hWaribiki)
    {
        this.hWaribiki = hWaribiki;
    }

    public int gethWaribiki()
    {
        return hWaribiki;
    }

    public String gethWaribiki_Inp()
    {
        return hWaribiki_Inp;
    }

    public void sethWaribiki_Inp(String hWaribiki_Inp)
    {
        this.hWaribiki_Inp = hWaribiki_Inp;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getBillOwnFlg()
    {
        return billOwnFlg;
    }

    public void setBillOwnFlg(int billOwnFlg)
    {
        this.billOwnFlg = billOwnFlg;
    }

    public int getImediaFlg()
    {
        return imediaFlg;
    }

    public void setImediaFlg(int imediaFlg)
    {
        this.imediaFlg = imediaFlg;
    }

    public String getSelYear()
    {
        return selYear;
    }

    public void setSelYear(String selYear)
    {
        this.selYear = selYear;
    }

    public String getSelMonth()
    {
        return selMonth;
    }

    public void setSelMonth(String selMonth)
    {
        this.selMonth = selMonth;
    }

    public String getSelDate()
    {
        return selDate;
    }

    public void setSelDate(String selDate)
    {
        this.selDate = selDate;
    }

    public int getInpUsageDate()
    {
        return inpUsageDate;
    }

    public void setInpUsageDate(int inpUsageDate)
    {
        this.inpUsageDate = inpUsageDate;
    }

    public String getInpPersonNm()
    {
        return inpPersonNm;
    }

    public void setInpPersonNm(String inpPersonNm)
    {
        this.inpPersonNm = inpPersonNm;
    }

    public String getInpHtRoomNo()
    {
        return inpHtRoomNo;
    }

    public void setInpHtRoomNo(String inpHtRoomNo)
    {
        this.inpHtRoomNo = inpHtRoomNo;
    }

    public String getInpUsageCharge()
    {
        return inpUsageCharge;
    }

    public void setInpUsageCharge(String inpUsageCharge)
    {
        this.inpUsageCharge = inpUsageCharge;
    }

    public String getInpRsvCharge()
    {
        return inpRsvCharge;
    }

    public void setInpRsvCharge(String inpRsvCharge)
    {
        this.inpRsvCharge = inpRsvCharge;
    }

    public String getInpWaribiki()
    {
        return inpWaribiki;
    }

    public void setInpWaribiki(String inpWaribiki)
    {
        this.inpWaribiki = inpWaribiki;
    }

    public String getSonota1Charge()
    {
        return sonota1Charge;
    }

    public void setSonota1Charge(String sonota1Charge)
    {
        this.sonota1Charge = sonota1Charge;
    }

    public String getSonota2Charget()
    {
        return sonota2Charget;
    }

    public void setSonota2Charget(String sonota2Charget)
    {
        this.sonota2Charget = sonota2Charget;
    }

    public void setSonota1(String sonota1)
    {
        this.sonota1 = sonota1;
    }

    public void setSonota2(String sonota2)
    {
        this.sonota2 = sonota2;
    }

    public String getSonota1()
    {
        return sonota1;
    }

    public String getSonota2()
    {
        return sonota2;
    }

    public int getClosingKind()
    {
        return closingKind;
    }

    public void setClosingKind(int closingKind)
    {
        this.closingKind = closingKind;
    }

    public int getsWaribikiInt()
    {
        return sWaribikiInt;
    }

    public void setsWaribikiInt(int sWaribikiInt)
    {
        this.sWaribikiInt = sWaribikiInt;
    }

    public String getHotenaviID()
    {
        return hotenaviID;
    }

    public void setHotenaviID(String hotenaviID)
    {
        this.hotenaviID = hotenaviID;
    }

    public String gethSeisan_View()
    {
        return hSeisan_View;
    }

    public void sethSeisan_View(String hSeisan_View)
    {
        this.hSeisan_View = hSeisan_View;
    }

    public String gethSeisan_Inp()
    {
        return hSeisan_Inp;
    }

    public void sethSeisan_Inp(String hSeisan_Inp)
    {
        this.hSeisan_Inp = hSeisan_Inp;
    }

    public int getModeFlg()
    {
        return modeFlg;
    }

    public void setModeFlg(int modeFlg)
    {
        this.modeFlg = modeFlg;
    }

    public int getSonotaFlg()
    {
        return sonotaFlg;
    }

    public void setSonotaFlg(int sonotaFlg)
    {
        this.sonotaFlg = sonotaFlg;
    }

    public boolean isExistsSeikyu()
    {
        return existsSeikyu;
    }

    public void setExistsSeikyu(boolean existsSeikyu)
    {
        this.existsSeikyu = existsSeikyu;
    }

    public int getBillSlipNo()
    {
        return billSlipNo;
    }

    public void setBillSlipNo(int billSlipNo)
    {
        this.billSlipNo = billSlipNo;
    }

    public double getBairitu()
    {
        return bairitu;
    }

    public void setBairitu(double bairitu)
    {
        this.bairitu = bairitu;
    }

    public int getCallPage()
    {
        return callPage;
    }

    public void setCallPage(int callPage)
    {
        this.callPage = callPage;
    }

    public String getSelYearFrom()
    {
        return selYearFrom;
    }

    public void setSelYearFrom(String selYearFrom)
    {
        this.selYearFrom = selYearFrom;
    }

    public String getSelMonthFrom()
    {
        return selMonthFrom;
    }

    public void setSelMonthFrom(String selMonthFrom)
    {
        this.selMonthFrom = selMonthFrom;
    }

    public String getSelDateFrom()
    {
        return selDateFrom;
    }

    public void setSelDateFrom(String selDateFrom)
    {
        this.selDateFrom = selDateFrom;
    }

    public String getSelYearTo()
    {
        return selYearTo;
    }

    public void setSelYearTo(String selYearTo)
    {
        this.selYearTo = selYearTo;
    }

    public String getSelMonthTo()
    {
        return selMonthTo;
    }

    public void setSelMonthTo(String selMonthTo)
    {
        this.selMonthTo = selMonthTo;
    }

    public String getSelDateTo()
    {
        return selDateTo;
    }

    public void setSelDateTo(String selDateTo)
    {
        this.selDateTo = selDateTo;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getNewAccSLipNo()
    {
        return newAccSLipNo;
    }

    public void setNewAccSLipNo(int newAccSLipNo)
    {
        this.newAccSLipNo = newAccSLipNo;
    }
}
