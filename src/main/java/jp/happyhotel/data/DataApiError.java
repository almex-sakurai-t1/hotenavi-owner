package jp.happyhotel.data;

/**
 * API���s���ʁi���s�j
 * 
 */
public class DataApiError extends DataApiResult
{
    /** ���s���� */
    private boolean success;
    /** �G���[ */
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
     * API�G���[���e
     * 
     */
    public class Error
    {
        /** �G���[�R�[�h */
        private String code;
        /** �G���[���b�Z�[�W */
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
