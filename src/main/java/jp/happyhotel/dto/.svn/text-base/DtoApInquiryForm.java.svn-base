package jp.happyhotel.dto;

import java.io.Serializable;

/**
 * 問合せフォーム
 * 
 * @author Shingo Tashiro
 * @version 1.00 2014/8/17
 */
public class DtoApInquiryForm implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -3548271416511726688L;

    private String            terminalId;                              // 端末ID
    private int               ciDate;                                  // チェックイン日付
    private int               ciTime;                                  // チェックイン時刻
    private String            userId;                                  // ユーザID
    private int               processedId;                             // 処理番号
    private int               errorCode;                               // エラーコード
    private DtoApCommon       apCommon;                                // タッチ共通データ
    private String            reserveNo;                               // 予約番号

    /**
     * データを初期化します。
     */
    public DtoApInquiryForm()
    {
        this.terminalId = "";
        this.ciDate = 0;
        this.ciTime = 0;
        this.userId = "";
        this.processedId = 0;
        this.errorCode = 0;
        this.reserveNo = "";
        this.apCommon = null;

    }

    public String getReserveNo()
    {
        return reserveNo;
    }

    public void setReserveNo(String reserveNo)
    {
        this.reserveNo = reserveNo;
    }

    public String getTerminalId()
    {
        return terminalId;
    }

    public void setTerminalId(String terminalId)
    {
        this.terminalId = terminalId;
    }

    public int getCiDate()
    {
        return ciDate;
    }

    public void setCiDate(int ciDate)
    {
        this.ciDate = ciDate;
    }

    public int getCiTime()
    {
        return ciTime;
    }

    public void setCiTime(int ciTime)
    {
        this.ciTime = ciTime;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getProcessedId()
    {
        return processedId;
    }

    public void setProcessedId(int processedId)
    {
        this.processedId = processedId;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public DtoApCommon getApCommon()
    {
        return apCommon;
    }

    public void setApCommon(DtoApCommon apCommon)
    {
        this.apCommon = apCommon;
    }

}
