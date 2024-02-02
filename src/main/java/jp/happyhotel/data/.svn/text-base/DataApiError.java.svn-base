package jp.happyhotel.data;

/**
 * API実行結果（失敗）
 * 
 */
public class DataApiError extends DataApiResult
{
    /** 実行結果 */
    private boolean success;
    /** エラー */
    private Error   error;

    public DataApiError()
    {
        this( null, null );
    }

    public DataApiError(int code, String message)
    {
        this( String.valueOf( code ), message );
    }

    public DataApiError(String code, String message)
    {
        this.success = false;
        error = new Error( code, message );
    }

    public boolean isSuccess()
    {
        return success;
    }

    public Error getError()
    {
        return error;
    }

    /**
     * APIエラー内容
     * 
     */
    public class Error
    {
        /** エラーコード */
        private String code;
        /** エラーメッセージ */
        private String message;

        public Error(String code, String message)
        {
            this.code = code;
            this.message = message;
        }

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public String getMessage()
        {
            return message;
        }

        public void setMessage(String message)
        {
            this.message = message;
        }
    }
}
