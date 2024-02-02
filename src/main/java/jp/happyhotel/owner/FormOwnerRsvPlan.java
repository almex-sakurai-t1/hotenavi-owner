package jp.happyhotel.owner;

import java.util.ArrayList;

/*
 * プラン設定Formクラス
 */
public class FormOwnerRsvPlan
{
    private int                                  selHotelID           = 0;                                         // 選択されているホテルID
    private int                                  selPlanID            = 0;                                         // 選択されているプランID
    private boolean                              isRsvPlan            = false;                                     // 予約データの存在フラグ(True:存在する、False:存在しない)
    private String                               dispIndex            = "";                                        // 表示順
    private String                               planNm               = "";                                        // プラン名
    private String                               planNmView           = "";
    private String                               dispStartDateView    = "";                                        // 表示開始日
    private String                               dispEndDateView      = "";
    private String                               dispStartDate        = "";
    private String                               dispEndDate          = "";
    private String                               orgDispStartDate     = "";                                        // 画面表示時の表示開始日
    private String                               orgDispEndDate       = "";
    private String                               salesStartDateView   = "";                                        // 販売開始日
    private String                               salesEndDateView     = "";
    private String                               salesStartDate       = "";
    private String                               orgSalesStartDate    = "";                                        // 画面表示時の販売開始日
    private String                               salesEndDate         = "";                                        // 販売終了日
    private int                                  salesFlag            = 0;                                         // 販売フラグ
    private String                               rsvStartDay          = "";                                        // 予約開始日
    private String                               rsvStartDayView      = "";
    private String                               rsvStartTimeHH       = "";                                        // 予約開始 HH
    private String                               rsvStartTimeMM       = "";                                        // 予約開始 MM
    private String                               rsvStartTimeView     = "";
    private String                               rsvEndDay            = "";                                        // 予約終了日
    private String                               rsvEndDayView        = "";
    private String                               rsvEndTimeHH         = "";                                        // 予約終了時間 HH
    private String                               rsvEndTimeMM         = "";                                        // 予約終了時間 MM
    private String                               rsvEndTimeView       = "";
    private String                               maxNumAdult          = "";                                        // 最大人数大人
    private int                                  maxNumAdultView      = 0;
    private String                               maxNumChild          = "";                                        // 最大人数子供
    private int                                  maxNumChildView      = 0;
    private String                               minNumAdult          = "";                                        // 最少人数大人
    private int                                  minNumAdultView      = 0;
    private String                               minNumChild          = "";                                        // 最少人数子供
    private int                                  minNumChildView      = 0;
    private int                                  minNumManView        = 0;
    private int                                  maxNumManView        = 0;
    private int                                  minNumWomanView      = 0;
    private int                                  maxNumWomanView      = 0;
    private String                               minNumMan            = "";                                        // 男性最小人数
    private String                               maxNumMan            = "";                                        // 男性最大人数
    private String                               minNumWoman          = "";                                        // 女性最小人数
    private String                               maxNumWoman          = "";                                        // 女性最大人数
    private String                               renpakuNum           = "";                                        // 連泊可能数
    private int                                  renpakuNumView       = 0;
    private int                                  pointKbn             = 0;                                         // ハピー付与
    private String                               pointRoom            = "";                                        // 室料に対する割合
    private String                               pointFix             = "";                                        // 固定ポイント
    private String                               hapyPointView        = "";                                        // ハピーポイント
    private String                               question             = "";                                        // 予約者へ質問
    private String                               questionView         = "";
    private int                                  questionFlag         = 0;                                         // 予約者への質問必須フラグ
    private String                               planInfo             = "";                                        // プラン紹介
    private String                               planInfoView         = "";
    private String                               remarks              = "";                                        // 備考
    private String                               remarksView          = "";
    private String                               planImg              = "";                                        // プラン画像
    private String                               planImgView          = "";
    private String                               maxQuantity          = "";                                        // 最大予約受付数
    private int                                  maxQuantityView      = 0;
    private int                                  offerKbn             = 0;                                         // 提供区分(部屋指定)
    private int                                  publishingFlg        = 0;                                         // 掲載フラグ
    private int                                  userRequestFlg       = 0;                                         // 要望入力項目フラグ
    private String                               salesBtnValue        = "";                                        // 一時的に停止する(販売中にする)ボタンのValue
    private String                               lowAdultTwoCharge    = "";                                        // 最低金額
    private String                               chargeInfo           = "";                                        // 料金
    private String                               ciTime               = "";                                        // チェックイン
    private String                               coTime               = "";                                        // チェックアウト
    private ArrayList<FormOwnerRsvPlanChargeSub> frmPlanChargeSubList = new ArrayList<FormOwnerRsvPlanChargeSub>(); // プラン別料金リスト
    private String                               chargeMsg            = "";                                        // 料金情報メッセージ
    private ArrayList<Integer>                   planSeq              = new ArrayList<Integer>();                  // 管理番号
    private String                               tekiyoRoom           = "";                                        // 適用部屋
    private int                                  mode                 = 0;                                         // 処理モード(1:新規追加、2：更新)
    private String                               mustOptions          = "";                                        // 必須オプション
    private String                               commOptions          = "";                                        // オプション
    private ArrayList<Integer>                   mustOptIdList        = new ArrayList<Integer>();                  // 必須オプションIDリスト
    private ArrayList<Integer>                   comOptIdList         = new ArrayList<Integer>();                  // 通常オプションIDリスト
    private ArrayList<String>                    chargeModeNameList   = new ArrayList<String>();                   // プランに紐付く料金モード名称リスト
    private ArrayList<Integer>                   chargeModeIdList     = new ArrayList<Integer>();                  // プランに紐付く料金モードIDリスト
    private ArrayList<Integer>                   ciFromList           = new ArrayList<Integer>();                  // ﾁｪｯｸｲﾝFROMリスト
    private ArrayList<Integer>                   ciToList             = new ArrayList<Integer>();                  // ﾁｪｯｸｲﾝTOリスト
    private ArrayList<Integer>                   coList               = new ArrayList<Integer>();                  // ﾁｪｯｸｱｳﾄリスト
    private int                                  manCountJudgeFlg     = 0;                                         // 男性多数許可フラグ

    private ArrayList<FormOwnerRsvPlanSub>       frmSubList           = new ArrayList<FormOwnerRsvPlanSub>();
    private ArrayList<FormOwnerRsvPlanOptionSub> frmMustOptionSubList = new ArrayList<FormOwnerRsvPlanOptionSub>(); // 必須オプション
    private ArrayList<FormOwnerRsvPlanOptionSub> frmCommOptionSubList = new ArrayList<FormOwnerRsvPlanOptionSub>(); // 任意オプション
    private int                                  draftMode            = 0;                                         // 下書きかどうか
    private String                               draftErrMsg          = "";
    private ArrayList<Integer>                   draftPlanIdList      = new ArrayList<Integer>();
    private ArrayList<String>                    draftPlanNmList      = new ArrayList<String>();
    private int                                  selDraftPlanId       = 0;
    private String                               selDraftPlanNm       = "";

    private String                               ownerHotelID         = "";
    private int                                  userId               = 0;
    private String                               errMsg               = "";

    private boolean                              isExistsRsv          = false;
    private String                               infoMsg              = "";
    private int                                  imediaFlg            = 0;
    private int                                  defaultPoint         = 0;
    private String                               premiumGoAheadDays   = "";
    private String                               lastUpdateTime       = "";
    private int                                  salesStopWeekStatus  = 0;

    public int getSelHotelID()
    {
        return selHotelID;
    }

    public void setSelHotelID(int selHotelID)
    {
        this.selHotelID = selHotelID;
    }

    public int getSelPlanID()
    {
        return selPlanID;
    }

    public void setSelPlanID(int selPlanID)
    {
        this.selPlanID = selPlanID;
    }

    public String getPlanNm()
    {
        return planNm;
    }

    public void setPlanNm(String planNm)
    {
        this.planNm = planNm;
    }

    public String getPlanNmView()
    {
        return planNmView;
    }

    public void setPlanNmView(String planNmView)
    {
        this.planNmView = planNmView;
    }

    public String getDispStartDateView()
    {
        return dispStartDateView;
    }

    public void setDispStartDateView(String dispStartDateView)
    {
        this.dispStartDateView = dispStartDateView;
    }

    public String getSalesStartDateView()
    {
        return salesStartDateView;
    }

    public void setSalesStartDateView(String salesStartDateView)
    {
        this.salesStartDateView = salesStartDateView;
    }

    public String getRsvStartDay()
    {
        return rsvStartDay;
    }

    public void setRsvStartDay(String rsvStartDay)
    {
        this.rsvStartDay = rsvStartDay;
    }

    public String getRsvStartDayView()
    {
        return rsvStartDayView;
    }

    public void setRsvStartDayView(String rsvStartDayView)
    {
        this.rsvStartDayView = rsvStartDayView;
    }

    public String getRsvStartTimeView()
    {
        return rsvStartTimeView;
    }

    public void setRsvStartTimeView(String rsvStartTimeView)
    {
        this.rsvStartTimeView = rsvStartTimeView;
    }

    public String getMaxNumAdult()
    {
        return maxNumAdult;
    }

    public void setMaxNumAdult(String maxNumAdult)
    {
        this.maxNumAdult = maxNumAdult;
    }

    public int getMaxNumAdultView()
    {
        return maxNumAdultView;
    }

    public void setMaxNumAdultView(int maxNumAdultView)
    {
        this.maxNumAdultView = maxNumAdultView;
    }

    public String getMaxNumChild()
    {
        return maxNumChild;
    }

    public void setMaxNumChild(String maxNumChild)
    {
        this.maxNumChild = maxNumChild;
    }

    public int getMaxNumChildView()
    {
        return maxNumChildView;
    }

    public void setMaxNumChildView(int maxNumChildView)
    {
        this.maxNumChildView = maxNumChildView;
    }

    public String getRenpakuNum()
    {
        return renpakuNum;
    }

    public void setRenpakuNum(String renpakuNum)
    {
        this.renpakuNum = renpakuNum;
    }

    public int getRenpakuNumView()
    {
        return renpakuNumView;
    }

    public void setRenpakuNumView(int renpakuNumView)
    {
        this.renpakuNumView = renpakuNumView;
    }

    public int getPointKbn()
    {
        return pointKbn;
    }

    public void setPointKbn(int pointKbn)
    {
        this.pointKbn = pointKbn;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public String getRsvEndDay()
    {
        return rsvEndDay;
    }

    public void setRsvEndDay(String rsvEndDay)
    {
        this.rsvEndDay = rsvEndDay;
    }

    public String getRsvEndDayView()
    {
        return rsvEndDayView;
    }

    public void setRsvEndDayView(String rsvEndDayView)
    {
        this.rsvEndDayView = rsvEndDayView;
    }

    public String getRsvEndTimeView()
    {
        return rsvEndTimeView;
    }

    public void setRsvEndTimeView(String rsvEndTimeView)
    {
        this.rsvEndTimeView = rsvEndTimeView;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getQuestionView()
    {
        return questionView;
    }

    public void setQuestionView(String questionView)
    {
        this.questionView = questionView;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getRemarksView()
    {
        return remarksView;
    }

    public void setRemarksView(String remarksView)
    {
        this.remarksView = remarksView;
    }

    public String getMaxQuantity()
    {
        return maxQuantity;
    }

    public void setMaxQuantity(String maxQuantity)
    {
        this.maxQuantity = maxQuantity;
    }

    public int getMaxQuantityView()
    {
        return maxQuantityView;
    }

    public void setMaxQuantityView(int maxQuantityView)
    {
        this.maxQuantityView = maxQuantityView;
    }

    public int getOfferKbn()
    {
        return offerKbn;
    }

    public void setOfferKbn(int offerKbn)
    {
        this.offerKbn = offerKbn;
    }

    public int getPublishingFlg()
    {
        return publishingFlg;
    }

    public void setPublishingFlg(int publishingFlg)
    {
        this.publishingFlg = publishingFlg;
    }

    public String getPointRoom()
    {
        return pointRoom;
    }

    public void setPointRoom(String pointRoom)
    {
        this.pointRoom = pointRoom;
    }

    public String getPointFix()
    {
        return pointFix;
    }

    public void setPointFix(String pointFix)
    {
        this.pointFix = pointFix;
    }

    public String getHapyPointView()
    {
        return hapyPointView;
    }

    public void setHapyPointView(String hapyPointView)
    {
        this.hapyPointView = hapyPointView;
    }

    public String getSalesEndDate()
    {
        return salesEndDate;
    }

    public void setSalesEndDate(String salesEndDate)
    {
        this.salesEndDate = salesEndDate;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getRsvStartTimeHH()
    {
        return rsvStartTimeHH;
    }

    public void setRsvStartTimeHH(String rsvStartTimeHH)
    {
        this.rsvStartTimeHH = rsvStartTimeHH;
    }

    public String getRsvStartTimeMM()
    {
        return rsvStartTimeMM;
    }

    public void setRsvStartTimeMM(String rsvStartTimeMM)
    {
        this.rsvStartTimeMM = rsvStartTimeMM;
    }

    public String getRsvEndTimeHH()
    {
        return rsvEndTimeHH;
    }

    public void setRsvEndTimeHH(String rsvEndTimeHH)
    {
        this.rsvEndTimeHH = rsvEndTimeHH;
    }

    public String getRsvEndTimeMM()
    {
        return rsvEndTimeMM;
    }

    public void setRsvEndTimeMM(String rsvEndTimeMM)
    {
        this.rsvEndTimeMM = rsvEndTimeMM;
    }

    public ArrayList<FormOwnerRsvPlanSub> getFrmSubList()
    {
        return frmSubList;
    }

    public void setFrmSubList(ArrayList<FormOwnerRsvPlanSub> frmSubList)
    {
        this.frmSubList = frmSubList;
    }

    public ArrayList<Integer> getPlanSeq()
    {
        return planSeq;
    }

    public void setPlanSeq(ArrayList<Integer> planSeq)
    {
        this.planSeq = planSeq;
    }

    public String getTekiyoRoom()
    {
        return tekiyoRoom;
    }

    public void setTekiyoRoom(String tekiyoRoom)
    {
        this.tekiyoRoom = tekiyoRoom;
    }

    public String getDispIndex()
    {
        return dispIndex;
    }

    public void setDispIndex(String dispIndex)
    {
        this.dispIndex = dispIndex;
    }

    public int getMode()
    {
        return mode;
    }

    public void setMode(int mode)
    {
        this.mode = mode;
    }

    public String getSalesBtnValue()
    {
        return salesBtnValue;
    }

    public void setSalesBtnValue(String salesBtnValue)
    {
        this.salesBtnValue = salesBtnValue;
    }

    public String getChargeInfo()
    {
        return chargeInfo;
    }

    public void setChargeInfo(String chargeInfo)
    {
        this.chargeInfo = chargeInfo;
    }

    public String getCiTime()
    {
        return ciTime;
    }

    public void setCiTime(String ciTime)
    {
        this.ciTime = ciTime;
    }

    public String getCoTime()
    {
        return coTime;
    }

    public void setCoTime(String coTime)
    {
        this.coTime = coTime;
    }

    public String getChargeMsg()
    {
        return chargeMsg;
    }

    public void setChargeMsg(String chargeMsg)
    {
        this.chargeMsg = chargeMsg;
    }

    public int getSalesFlag()
    {
        return salesFlag;
    }

    public void setSalesFlag(int salesFlag)
    {
        this.salesFlag = salesFlag;
    }

    public String getDispStartDate()
    {
        return dispStartDate;
    }

    public void setDispStartDate(String dispStartDate)
    {
        this.dispStartDate = dispStartDate;
    }

    public String getSalesStartDate()
    {
        return salesStartDate;
    }

    public void setSalesStartDate(String salesStartDate)
    {
        this.salesStartDate = salesStartDate;
    }

    public String getDispEndDate()
    {
        return dispEndDate;
    }

    public void setDispEndDate(String dispEndDate)
    {
        this.dispEndDate = dispEndDate;
    }

    public ArrayList<FormOwnerRsvPlanOptionSub> getFrmMustOptionSubList()
    {
        return frmMustOptionSubList;
    }

    public void setFrmMustOptionSubList(ArrayList<FormOwnerRsvPlanOptionSub> frmMustOptionSubList)
    {
        this.frmMustOptionSubList = frmMustOptionSubList;
    }

    public ArrayList<FormOwnerRsvPlanOptionSub> getFrmCommOptionSubList()
    {
        return frmCommOptionSubList;
    }

    public void setFrmCommOptionSubList(ArrayList<FormOwnerRsvPlanOptionSub> frmCommOptionSubList)
    {
        this.frmCommOptionSubList = frmCommOptionSubList;
    }

    public String getMustOptions()
    {
        return mustOptions;
    }

    public void setMustOptions(String mustOptions)
    {
        this.mustOptions = mustOptions;
    }

    public String getCommOptions()
    {
        return commOptions;
    }

    public void setCommOptions(String commOptions)
    {
        this.commOptions = commOptions;
    }

    public ArrayList<Integer> getMustOptIdList()
    {
        return mustOptIdList;
    }

    public void setMustOptIdList(ArrayList<Integer> mustOptIdList)
    {
        this.mustOptIdList = mustOptIdList;
    }

    public ArrayList<Integer> getComOptIdList()
    {
        return comOptIdList;
    }

    public void setComOptIdList(ArrayList<Integer> comOptIdList)
    {
        this.comOptIdList = comOptIdList;
    }

    public String getPlanInfo()
    {
        return planInfo;
    }

    public void setPlanInfo(String planInfo)
    {
        this.planInfo = planInfo;
    }

    public String getPlanInfoView()
    {
        return planInfoView;
    }

    public void setPlanInfoView(String planInfoView)
    {
        this.planInfoView = planInfoView;
    }

    public boolean isRsvPlan()
    {
        return isRsvPlan;
    }

    public void setRsvPlan(boolean isRsvPlan)
    {
        this.isRsvPlan = isRsvPlan;
    }

    public String getOrgDispStartDate()
    {
        return orgDispStartDate;
    }

    public void setOrgDispStartDate(String orgDispStartDate)
    {
        this.orgDispStartDate = orgDispStartDate;
    }

    public String getOrgDispEndDate()
    {
        return orgDispEndDate;
    }

    public void setOrgDispEndDate(String orgDispEndDate)
    {
        this.orgDispEndDate = orgDispEndDate;
    }

    public String getOrgSalesStartDate()
    {
        return orgSalesStartDate;
    }

    public void setOrgSalesStartDate(String orgSalesStartDate)
    {
        this.orgSalesStartDate = orgSalesStartDate;
    }

    public String getDispEndDateView()
    {
        return dispEndDateView;
    }

    public void setDispEndDateView(String dispEndDateView)
    {
        this.dispEndDateView = dispEndDateView;
    }

    public String getSalesEndDateView()
    {
        return salesEndDateView;
    }

    public void setSalesEndDateView(String salesEndDateView)
    {
        this.salesEndDateView = salesEndDateView;
    }

    public String getPlanImg()
    {
        return planImg;
    }

    public void setPlanImg(String planImg)
    {
        this.planImg = planImg;
    }

    public String getPlanImgView()
    {
        return planImgView;
    }

    public void setPlanImgView(String planImgView)
    {
        this.planImgView = planImgView;
    }

    public int getDraftMode()
    {
        return draftMode;
    }

    public void setDraftMode(int draftMode)
    {
        this.draftMode = draftMode;
    }

    public ArrayList<Integer> getDraftPlanIdList()
    {
        return draftPlanIdList;
    }

    public void setDraftPlanIdList(ArrayList<Integer> draftPlanIdList)
    {
        this.draftPlanIdList = draftPlanIdList;
    }

    public ArrayList<String> getDraftPlanNmList()
    {
        return draftPlanNmList;
    }

    public void setDraftPlanNmList(ArrayList<String> draftPlanNmList)
    {
        this.draftPlanNmList = draftPlanNmList;
    }

    public String getDraftErrMsg()
    {
        return draftErrMsg;
    }

    public void setDraftErrMsg(String draftErrMsg)
    {
        this.draftErrMsg = draftErrMsg;
    }

    public String getSelDraftPlanNm()
    {
        return selDraftPlanNm;
    }

    public void setSelDraftPlanNm(String selDraftPlanNm)
    {
        this.selDraftPlanNm = selDraftPlanNm;
    }

    public int getSelDraftPlanId()
    {
        return selDraftPlanId;
    }

    public void setSelDraftPlanId(int selDraftPlanId)
    {
        this.selDraftPlanId = selDraftPlanId;
    }

    public boolean isExistsRsv()
    {
        return isExistsRsv;
    }

    public void setExistsRsv(boolean isExistsRsv)
    {
        this.isExistsRsv = isExistsRsv;
    }

    public String getInfoMsg()
    {
        return infoMsg;
    }

    public void setInfoMsg(String infoMsg)
    {
        this.infoMsg = infoMsg;
    }

    public int getImediaFlg()
    {
        return imediaFlg;
    }

    public void setImediaFlg(int imediaFlg)
    {
        this.imediaFlg = imediaFlg;
    }

    public int getDefaultPoint()
    {
        return defaultPoint;
    }

    public void setDefaultPoint(int defaultPoint)
    {
        this.defaultPoint = defaultPoint;
    }

    public String getOwnerHotelID()
    {
        return ownerHotelID;
    }

    public void setOwnerHotelID(String ownerHotelID)
    {
        this.ownerHotelID = ownerHotelID;
    }

    public void setPremiumGoAheadDays(String premiumGoAheadDays)
    {
        this.premiumGoAheadDays = premiumGoAheadDays;
    }

    public String getPremiumGoAheadDays()
    {
        return premiumGoAheadDays;
    }

    public void setMinNumAdult(String minNumAdult)
    {
        this.minNumAdult = minNumAdult;
    }

    public String getMinNumAdult()
    {
        return minNumAdult;
    }

    public void setMinNumAdultView(int minNumAdultView)
    {
        this.minNumAdultView = minNumAdultView;
    }

    public int getMinNumAdultView()
    {
        return minNumAdultView;
    }

    public void setMinNumChild(String minNumChild)
    {
        this.minNumChild = minNumChild;
    }

    public String getMinNumChild()
    {
        return minNumChild;
    }

    public void setMinNumChildView(int minNumChildView)
    {
        this.minNumChildView = minNumChildView;
    }

    public int getMinNumChildView()
    {
        return minNumChildView;
    }

    public void setFrmPlanChargeSubList(ArrayList<FormOwnerRsvPlanChargeSub> frmPlanChargeSubList)
    {
        this.frmPlanChargeSubList = frmPlanChargeSubList;
    }

    public ArrayList<FormOwnerRsvPlanChargeSub> getFrmPlanChargeSubList()
    {
        return frmPlanChargeSubList;
    }

    public void setLowAdultTwoCharge(String lowAdultTwoCharge)
    {
        this.lowAdultTwoCharge = lowAdultTwoCharge;
    }

    public String getLowAdultTwoCharge()
    {
        return lowAdultTwoCharge;
    }

    public void setUserRequestFlg(int userRequestFlg)
    {
        this.userRequestFlg = userRequestFlg;
    }

    public int getUserRequestFlg()
    {
        return userRequestFlg;
    }

    public void setQuestionFlag(int questionFlag)
    {
        this.questionFlag = questionFlag;
    }

    public int getQuestionFlag()
    {
        return questionFlag;
    }

    public void setChargeModeNameList(ArrayList<String> chargeModeNameList)
    {
        this.chargeModeNameList = chargeModeNameList;
    }

    public ArrayList<String> getChargeModeNameList()
    {
        return chargeModeNameList;
    }

    public void setChargeModeIdList(ArrayList<Integer> chargeModeIdList)
    {
        this.chargeModeIdList = chargeModeIdList;
    }

    public ArrayList<Integer> getChargeModeIdList()
    {
        return chargeModeIdList;
    }

    public void setCiFromList(ArrayList<Integer> ciFromList)
    {
        this.ciFromList = ciFromList;
    }

    public ArrayList<Integer> getCiFromList()
    {
        return ciFromList;
    }

    public void setCiToList(ArrayList<Integer> ciToList)
    {
        this.ciToList = ciToList;
    }

    public ArrayList<Integer> getCiToList()
    {
        return ciToList;
    }

    public void setCoList(ArrayList<Integer> coList)
    {
        this.coList = coList;
    }

    public ArrayList<Integer> getCoList()
    {
        return coList;
    }

    public void setMinNumManView(int minNumManView)
    {
        this.minNumManView = minNumManView;
    }

    public int getMinNumManView()
    {
        return minNumManView;
    }

    public void setMaxNumManView(int maxNumManView)
    {
        this.maxNumManView = maxNumManView;
    }

    public int getMaxNumManView()
    {
        return maxNumManView;
    }

    public void setMinNumWomanView(int minNumWomanView)
    {
        this.minNumWomanView = minNumWomanView;
    }

    public int getMinNumWomanView()
    {
        return minNumWomanView;
    }

    public void setMaxNumWomanView(int maxNumWomanView)
    {
        this.maxNumWomanView = maxNumWomanView;
    }

    public int getMaxNumWomanView()
    {
        return maxNumWomanView;
    }

    public void setMinNumMan(String minNumMan)
    {
        this.minNumMan = minNumMan;
    }

    public String getMinNumMan()
    {
        return minNumMan;
    }

    public void setMaxNumMan(String maxNumMan)
    {
        this.maxNumMan = maxNumMan;
    }

    public String getMaxNumMan()
    {
        return maxNumMan;
    }

    public void setMinNumWoman(String minNumWoman)
    {
        this.minNumWoman = minNumWoman;
    }

    public String getMinNumWoman()
    {
        return minNumWoman;
    }

    public void setMaxNumWoman(String maxNumWoman)
    {
        this.maxNumWoman = maxNumWoman;
    }

    public String getMaxNumWoman()
    {
        return maxNumWoman;
    }

    public void setManCountJudgeFlg(int manCountJudgeFlg)
    {
        this.manCountJudgeFlg = manCountJudgeFlg;
    }

    public int getManCountJudgeFlg()
    {
        return manCountJudgeFlg;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getSalesStopWeekStatus() {
        return salesStopWeekStatus;
    }

    public void setSalesStopWeekStatus(int salesStopWeekStatus) {
        this.salesStopWeekStatus = salesStopWeekStatus;
    }
}
