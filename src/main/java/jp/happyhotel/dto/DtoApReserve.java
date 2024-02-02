package jp.happyhotel.dto;

import java.io.Serializable;


/**
 * 予約データ
 *
 * @author Shingo Tashiro
 * @version 1.00 2014/8/17
 */
public class DtoApReserve implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 3085164140739761338L;

    private String            reserveNo;                              // 予約番号
    private boolean           rsvResult;                              // 予約結果
    private boolean           existsRsv;                              // 予約有無
    private int               errorCode;                              // エラーコード
    private int               hostKind;                               // ホスト種別
    private DtoApCommon       apCommon;                               // タッチ共通データ

    /**
     * データを初期化します。
     */
    public DtoApReserve()
    {
        this.reserveNo = "";
        this.rsvResult = false;
        this.existsRsv = false;
        this.errorCode = 0;
        this.hostKind = 0;
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

    public boolean isRsvResult()
    {
        return rsvResult;
    }

    public void setRsvResult(boolean rsvResult)
    {
        this.rsvResult = rsvResult;
    }

    public boolean isExistsRsv() {
		return existsRsv;
	}

	public void setExistsRsv(boolean existsRsv) {
		this.existsRsv = existsRsv;
	}

	public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public int getHostKind()
    {
        return hostKind;
    }

    public void setHostKind(int hostKind)
    {
        this.hostKind = hostKind;
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
