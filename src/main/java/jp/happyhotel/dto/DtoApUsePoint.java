package jp.happyhotel.dto;

import java.io.Serializable;


/**
 * ユーザ使用マイル
 *
 * @author Shingo Tashiro
 * @version 1.00 2014/8/17
 */
public class DtoApUsePoint implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 8626897607780507343L;

    private boolean           result;                                 // 処理結果
    private int               userPoint;                              // 使用マイル
    private int               errorCode;                              // エラーコード
    private String            errorMessage;                           // エラーメッセージ
	private DtoApCommon       apCommon;                               // タッチ共通データ

    /**
     * データを初期化します。
     */
    public DtoApUsePoint()
    {
    	this.result = false;
        this.userPoint = 0;
        this.errorCode = 0;
        this.errorMessage = "";
        this.apCommon = null;
    }

    public int getUserPoint()
    {
        return userPoint;
    }

    public void setUserPoint(int userPoint)
    {
        this.userPoint = userPoint;
    }

    public DtoApCommon getApCommon()
    {
        return apCommon;
    }

    public void setApCommon(DtoApCommon apCommon)
    {
        this.apCommon = apCommon;
    }

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
