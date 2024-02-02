package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 外部サービス定義データ操作用クラス<br>
 * <br>
 * 外部サービス（Facebook、GoogleMap、LINE@、etc...）の定義データ操作用のクラスです。<br>
 * 
 * @author koshiba-y1
 */
public class DataHhMasterUrl implements Serializable
{
    /** 外部サービスの種類を示す識別子 */
    private int    data_type;
    /** サービス名 */
    private String name;
    /** ボタン要素HTMLテキスト */
    private String button_image_elem;
    /** ボタン要素HTMLテキスト（スマホサイト用） */
    private String button_image_elem_phone;
    /** URLのバリデーションチェック用JS */
    private String val_check_script;
    /** 事業部のみ編集可能なコンテンツを表すフラグ */
    private int    imedia_only;

    /**
     * コンストラクタ
     */
    public DataHhMasterUrl()
    {
        // インスタンス変数初期化
        this.data_type = 0;
        this.name = "";
        this.button_image_elem = "";
        this.button_image_elem_phone = "";
        this.val_check_script = "";
        this.imedia_only = 0;
    }

    /**
     * this.data_typeのゲッター
     * 
     * @return this.data_type
     */
    public int getDataType()
    {
        return this.data_type;
    }

    /**
     * this.nameのゲッター
     * 
     * @return this.name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * this.button_image_elemのゲッター
     * 
     * @return this.button_image_elem
     */
    public String getButtonImageElem()
    {
        return this.button_image_elem;
    }

    /**
     * this.button_image_elem_phoneのゲッター
     * 
     * @return button_image_elem_phone
     */
    public String getButtonImageElemPhone()
    {
        return this.button_image_elem_phone;
    }

    /**
     * this.val_check_scriptのゲッター
     * 
     * @return this.val_check_script
     */
    public String getValCheckScript()
    {
        return this.val_check_script;
    }

    /**
     * this.imedia_onlyのゲッター
     * 
     * @return this.imedia_only
     */
    public int getImediaOnly()
    {
        return this.imedia_only;
    }

    /**
     * this.data_typeのセッター
     * 
     * @param data_type 外部サービスの種類を示す識別子
     */
    public void setDataType(int data_type)
    {
        this.data_type = data_type;
    }

    /**
     * this.nameのセッター
     * 
     * @param name サービス名
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * this.button_image_elemのセッター
     * 
     * @param button_image_elem ボタン要素HTMLテキスト
     */
    public void setButtonImageElem(String button_image_elem)
    {
        this.button_image_elem = button_image_elem;
    }

    /**
     * this.button_image_elem_phoneのセッター
     * 
     * @param button_image_elem_phone ボタン要素HTMLテキスト
     */
    public void setButtonImageElemPhone(String button_image_elem_phone)
    {
        this.button_image_elem_phone = button_image_elem_phone;
    }

    /**
     * this.val_check_scriptのセッター
     * 
     * @param val_check_script URLのバリデーションチェック用JS
     */
    public void setValCheckScript(String val_check_script)
    {
        this.val_check_script = val_check_script;
    }

    /**
     * this.imedia_onlyのセッター
     * 
     * @param imedia_only 事業部のみ編集可能なコンテンツを表すフラグ
     */
    public void setImediaOnly(int imedia_only)
    {
        this.imedia_only = imedia_only;
    }

    /**
     * 全てのインスタンス変数へのセッター
     * 
     * @param result データを含む行を示したResultSet
     * @return 処理の結果
     * @throws Exception
     */
    public boolean setData(ResultSet result) throws Exception
    {
        if ( result == null )
        {
            return false;
        }

        try
        {
            this.data_type = result.getInt( "data_type" );
            this.name = result.getString( "name" );
            this.button_image_elem = result.getString( "button_image_elem" );
            this.button_image_elem_phone = result.getString( "button_image_elem_phone" );
            this.val_check_script = result.getString( "val_check_script" );
            this.imedia_only = result.getInt( "imedia_only" );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhMasterUrl.setData] Exception=" + e.toString() );
            throw e;
        }

        return true;
    }

    /**
     * データ取得<br>
     * <br>
     * hh_master_urlからデータを取得し、インスタンス変数にセットします。<br>
     * データを取得できた場合はtrueを、取得できなかった場合はfalseを返します。<br>
     * BDへのアクセス等でエラーが発生した場合は例外を投げます。<br>
     * 
     * @param data_type 外部サービスの種類を示す識別子
     * @return 処理の結果
     * @exception Exception
     */
    public boolean selectData(int data_type) throws Exception
    {
        // DBアクセス関連
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( "SELECT * FROM hh_master_url WHERE data_type = " + data_type );
            result = prestate.executeQuery();

            if ( result == null )
            {
                return false;
            }

            if ( result.next() == false )
            {
                return false;
            }

            this.data_type = result.getInt( "data_type" );
            this.name = result.getString( "name" );
            this.button_image_elem = result.getString( "button_image_elem" );
            this.button_image_elem_phone = result.getString( "button_image_elem_phone" );
            this.val_check_script = result.getString( "val_check_script" );
            this.imedia_only = result.getInt( "imedia_only" );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhMasterUrl.selectData] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return true;
    }

    /**
     * hh_master_urlに登録されている外部サービス数の取得
     * 
     * @return hh_master_urlに登録されている外部サービスの数
     * @throws Exception
     */
    public int selectServicesCount() throws Exception
    {
        // DBアクセス関連
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( "SELECT COUNT(data_type) AS num FROM hh_master_url" );
            result = prestate.executeQuery();

            if ( result == null )
            {
                return 0;
            }

            if ( result.next() == false )
            {
                return 0;
            }

            return result.getInt( "num" );
        }
        catch ( Exception e )
        {
            Logging.error( "[DataHhMasterUrl.selectServicesCount] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
    }
}
