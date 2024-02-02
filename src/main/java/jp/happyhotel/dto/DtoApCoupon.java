package jp.happyhotel.dto;

import java.io.Serializable;

/**
 * ユーザデータ
 * 
 * @author Shingo Tashiro
 * @version 1.00 2014/8/17
 */
public class DtoApCoupon implements Serializable
{
    private static final long serialVersionUID = -5481514313177311902L;

    private boolean           existsCoupon;                            // 対象ホテルクーポン所持不所持
    private boolean           result;                                  // クーポン処理結果
    private boolean           useable;                                 // クーポン利用可否
    private String            unavailableKind;                         // クーポン利用不可理由
    private String            unavailableMessage;                      // クーポン利用不可要因
    private int               errorCode;                               // クーポン使用エラーコード
    private String            errorMessage;                            // クーポン使用エラーメッセージ
    private DtoApCommon       apCommon;                                // タッチ共通データ

    // TODO ここにクーポン情報を追加

    /**
     * データを初期化します。
     */
    public DtoApCoupon()
    {
        this.existsCoupon = false;
        this.result = false;
        this.useable = false;
        this.unavailableKind = "";
        this.unavailableMessage = "";
        this.errorCode = 0;
        this.errorMessage = "";
        this.apCommon = null;
    }

    public boolean isExistsCoupon()
    {
        return existsCoupon;
    }

    public void setExistsCoupon(boolean existsCoupon)
    {
        this.existsCoupon = existsCoupon;
    }

    public boolean isResult()
    {
        return result;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public boolean isUseable()
    {
        return useable;
    }

    public void setUseable(boolean useable)
    {
        this.useable = useable;
    }

    public String getUnavailableKind()
    {
        return unavailableKind;
    }

    public void setUnavailableKind(String unavailableKind)
    {
        this.unavailableKind = unavailableKind;
    }

    public String getUnavailableMessage()
    {
        return unavailableMessage;
    }

    public void setUnavailableMessage(String unavailableMessage)
    {
        this.unavailableMessage = unavailableMessage;
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

    public DtoApCommon getApCommon()
    {
        return apCommon;
    }

    public void setApCommon(DtoApCommon apCommon)
    {
        this.apCommon = apCommon;
    }

}
