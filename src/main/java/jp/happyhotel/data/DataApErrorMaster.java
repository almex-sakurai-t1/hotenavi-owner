package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * エラーマスタ(ap_error_master)取得クラス
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApErrorMaster implements Serializable
{
    /**
     * 
     */
    private static final long  serialVersionUID = 209048489932795380L;
    public static final String TABLE            = "ap_error_master";
    private int                errorCode;                             // エラーコード
    private int                errorSub;                              // エラーサブコード
    private String             errorKind;                             // エラー種類
    private String             errorMessage;                          // エラー内容
    private String             dispMessage;                           // エラー表示
    private String             measures;                              // 対応策

    /**
     * データを初期化します。
     */
    public DataApErrorMaster()
    {
        this.errorCode = 0;
        this.errorSub = 0;
        this.errorKind = "";
        this.errorMessage = "";
        this.dispMessage = "";
        this.measures = "";
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public int getErrorSub()
    {
        return errorSub;
    }

    public String getErrorKind()
    {
        return errorKind;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public String getDispMessage()
    {
        return dispMessage;
    }

    public String getMeasures()
    {
        return measures;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public void setErrorSub(int errorSub)
    {
        this.errorSub = errorSub;
    }

    public void setErrorKind(String errorKind)
    {
        this.errorKind = errorKind;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public void setDispMessage(String dispMessage)
    {
        this.dispMessage = dispMessage;
    }

    public void setMeasures(String measures)
    {
        this.measures = measures;
    }

    /****
     * エラーマスタ(ap_error_master)取得
     * 
     * @param errorCode エラーコード
     * @param errorSub エラーサブコード
     * @return
     */
    public boolean getData(int errorCode, int errorSub)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_error_master WHERE error_code = ? AND error_sub = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, errorCode );
            prestate.setInt( 2, errorSub );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.errorCode = result.getInt( "error_code" );
                    this.errorSub = result.getInt( "error_sub" );
                    this.errorKind = result.getString( "error_kind" );
                    this.errorMessage = result.getString( "error_message" );
                    this.dispMessage = result.getString( "disp_message" );
                    this.measures = result.getString( "measures" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApErrorMaster.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * エラーマスタ(ap_error_master)設定
     * 
     * @param result マスターソートレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.errorCode = result.getInt( "error_code" );
                this.errorSub = result.getInt( "error_sub" );
                this.errorKind = result.getString( "error_kind" );
                this.errorMessage = result.getString( "error_message" );
                this.dispMessage = result.getString( "disp_message" );
                this.measures = result.getString( "measures" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApErrorMaster.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * エラーマスタ(ap_error_master)挿入
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */

    public boolean insertData()
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;

        query = "INSERT ap_error_master SET ";
        query += " error_code=?";
        query += ", error_sub=?";
        query += ", error_kind=?";
        query += ", error_message=?";
        query += ", disp_message=?";
        query += ", measures=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.errorCode );
            prestate.setInt( i++, this.errorSub );
            prestate.setString( i++, this.errorKind );
            prestate.setString( i++, this.errorMessage );
            prestate.setString( i++, this.dispMessage );
            prestate.setString( i++, this.measures );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApErrorMaster.insertData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }

    /**
     * エラーマスタ(ap_error_master)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param errorCode エラーコード
     * @param errorSub エラーサブコード
     * @return
     */
    public boolean updateData(int errorCode, int errorSub)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_error_master SET ";
        query += " error_kind=?";
        query += ", error_message=?";
        query += ", disp_message=?";
        query += ", measures=?";
        query += " WHERE error_code=? AND error_sub=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.errorKind );
            prestate.setString( i++, this.errorMessage );
            prestate.setString( i++, this.dispMessage );
            prestate.setString( i++, this.measures );
            prestate.setInt( i++, this.errorCode );
            prestate.setInt( i++, this.errorSub );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApErrorMaster.updateData] Exception=" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
        return(ret);
    }
}
