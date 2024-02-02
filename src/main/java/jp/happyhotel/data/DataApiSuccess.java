package jp.happyhotel.data;

/**
 * API実行結果（成功）
 * 
 */
public class DataApiSuccess extends DataApiResult
{
    /** 実行結果 */
    private boolean success = true;

    public boolean isSuccess()
    {
        return success;
    }

}
