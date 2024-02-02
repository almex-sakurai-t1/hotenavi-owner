package jp.happyhotel.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.data.DataHotelUrl;

/**
 * 外部サービスURL管理用クラス<br>
 * <br>
 * 外部サービス（Facebook、GoogleMap、LINE@、etc...）のURLを管理するクラスです。<br>
 * DBにデータを登録したり、DBからデータを取り出したりする機能を提供します。<br>
 * 
 * @author koshiba-y1
 */
public class ExternalServiceUrl extends DataHotelUrl implements Serializable
{
    /**
     * コンストラクタ
     */
    public ExternalServiceUrl()
    {
        // 特になし
    }

    /**
     * コンストラクタ
     * 
     * @param id ホテルID
     */
    public ExternalServiceUrl(int id)
    {
        this.id = id;
    }

    /**
     * インスタンス変数の初期化
     */
    private void initInstanceVariable()
    {
        this.id = 0;
        this.seq = 0;
        this.url = "";
        this.dataType = 0;
        this.delFlag = 0;
        this.startDate = 0;
        this.endDate = 0;
    }

    /**
     * クエリの実行<br>
     * <br>
     * hh_hotel_urlに関するSELECT文を実行し、結果をインスタンス変数に書き込みます。<br>
     * 結果が取得できた場合はtrueを、取得できなかった場合はfalseを返します。<br>
     * 
     * @param query 実行するクエリ
     * @return 実行結果
     * @throws Exception
     */
    private boolean executeQuery(String query) throws Exception
    {
        // DBアクセス関連
        Connection connection = null;
        PreparedStatement prestate = null;
        ResultSet result = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            result = prestate.executeQuery();

            if ( result == null )
            {
                initInstanceVariable();
                return false;
            }

            if ( result.next() == false )
            {
                initInstanceVariable();
                return false;
            }

            this.id = result.getInt( "id" );
            this.seq = result.getInt( "seq" );
            this.url = result.getString( "url" );
            this.dataType = result.getInt( "data_type" );
            this.delFlag = result.getInt( "del_flag" );
            this.startDate = result.getInt( "start_date" );
            this.endDate = result.getInt( "end_date" );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        return true;
    }

    /**
     * URL（を含む行データ）の取得処理<br>
     * <br>
     * DB（hh_hotel_url）から該当するURLを含む行データを取得し、URLを返します。<br>
     * del_flagの値は考慮しません。<br>
     * 行が存在しなかった場合は空文字を返します。<br>
     * 行のデータはインスタンス変数に格納されます。<br>
     * 
     * @param id ホテルID
     * @param data_type サービスを表す識別子
     * @param now_date 更新日として記録する日付（YYYYMMDD）
     * @return 外部サービスのURL
     * @throws Exception
     */
    public String selectUrlRow(int id, int data_type, int now_date) throws Exception
    {
        // クエリ生成
        String query = "";
        query += "SELECT * FROM hh_hotel_url WHERE id=" + id;
        query += " AND data_type = " + data_type;
        query += " AND start_date <= " + now_date;
        query += " AND end_date >= " + now_date;
        query += " ORDER BY seq DESC";

        // クエリの実行
        try
        {
            if ( this.executeQuery( query ) == false )
            {
                return "";
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.selectUrlRow] Exception=" + e.toString() );
            throw e;
        }

        return this.url;
    }

    /**
     * URL（を含む行データ）の取得処理<br>
     * <br>
     * DB（hh_hotel_url）から該当するURLを含む行データを取得し、URLを返します。<br>
     * del_flagの値は考慮しません。<br>
     * 行が存在しなかった場合は空文字を返します。<br>
     * 行のデータはインスタンス変数に格納されます。<br>
     * 
     * @param id ホテルID
     * @param data_type サービスを表す識別子
     * @return 外部サービスのURL
     * @throws Exception
     */
    public String selectUrlRow(int id, int data_type) throws Exception
    {
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        return this.selectUrlRow( id, data_type, now_date );
    }

    /**
     * URL（を含む行データ）の取得処理<br>
     * <br>
     * DB（hh_hotel_url）から該当するURLを含む行データを取得し、URLを返します。<br>
     * del_flagの値は考慮しません。<br>
     * 行が存在しなかった場合は空文字を返します。<br>
     * 行のデータはインスタンス変数に格納されます。<br>
     * 
     * @param data_type サービスを表す識別子
     * @return 外部サービスのURL
     * @throws Exception
     */
    public String selectUrlRow(int data_type) throws Exception
    {
        return this.selectUrlRow( this.id, data_type );
    }

    /**
     * URL（を含む行データ）の取得処理<br>
     * <br>
     * DB（hh_hotel_url）から該当するURLを含む行データを取得し、URLを返します。<br>
     * del_flagが0のものに関して取得します。<br>
     * 行が存在しなかった場合は空文字を返します。<br>
     * 行のデータはインスタンス変数に格納されます。<br>
     * 
     * @param id ホテルID
     * @param data_type サービスを表す識別子
     * @param now_date 更新日として記録する日付（YYYYMMDD）
     * @return 外部サービスのURL
     * @throws Exception
     */
    public String selectNotDeletedUrlRow(int id, int data_type, int now_date) throws Exception
    {
        // クエリ生成
        String query = "";
        query += "SELECT * FROM hh_hotel_url WHERE id=" + id;
        query += " AND data_type = " + data_type;
        query += " AND start_date <= " + now_date;
        query += " AND end_date >= " + now_date;
        query += " AND del_flag = 0";
        query += " ORDER BY seq DESC";

        // クエリの実行
        try
        {
            if ( this.executeQuery( query ) == false )
            {
                return "";
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.selectNotDeletedUrlRow] Exception=" + e.toString() );
            throw e;
        }

        return this.url;
    }

    /**
     * URL（を含む行データ）の取得処理<br>
     * <br>
     * DB（hh_hotel_url）から該当するURLを含む行データを取得し、URLを返します。<br>
     * del_flagが0のものに関して取得します。<br>
     * 行が存在しなかった場合は空文字を返します。<br>
     * 行のデータはインスタンス変数に格納されます。<br>
     * 
     * @param id ホテルID
     * @param data_type サービスを表す識別子
     * @return 外部サービスのURL
     * @throws Exception
     */
    public String selectNotDeletedUrlRow(int id, int data_type) throws Exception
    {
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        return this.selectNotDeletedUrlRow( id, data_type, now_date );
    }

    /**
     * URL（を含む行データ）の取得処理<br>
     * <br>
     * DB（hh_hotel_url）から該当するURLを含む行データを取得し、URLを返します。<br>
     * del_flagが0のものに関して取得します。<br>
     * 行が存在しなかった場合は空文字を返します。<br>
     * 行のデータはインスタンス変数に格納されます。<br>
     * 
     * @param data_type サービスを表す識別子
     * @return 外部サービスのURL
     * @throws Exception
     */
    public String selectNotDeletedUrlRow(int data_type) throws Exception
    {
        return this.selectNotDeletedUrlRow( this.id, data_type );
    }

    /**
     * URL（を含む行データ）の取得処理<br>
     * <br>
     * DB（hh_hotel_url）から該当するURLを含む行データを取得し、URLを返します。<br>
     * del_flagが1のものに関して取得します。<br>
     * 行が存在しなかった場合は空文字を返します。<br>
     * 行のデータはインスタンス変数に格納されます。<br>
     * 
     * @param id ホテルID
     * @param data_type サービスを表す識別子
     * @param now_date 更新日として記録する日付（YYYYMMDD）
     * @return 外部サービスのURL
     * @throws Exception
     */
    public String selectDeletedUrlRow(int id, int data_type, int now_date) throws Exception
    {
        // クエリ生成
        String query = "";
        query += "SELECT * FROM hh_hotel_url WHERE id=" + id;
        query += " AND data_type = " + data_type;
        query += " AND start_date <= " + now_date;
        query += " AND end_date >= " + now_date;
        query += " AND del_flag = 1";
        query += " ORDER BY seq DESC";

        // クエリの実行
        try
        {
            if ( this.executeQuery( query ) == false )
            {
                return "";
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.selectDeletedUrlRow] Exception=" + e.toString() );
            throw e;
        }

        return this.url;
    }

    /**
     * URL（を含む行データ）の取得処理<br>
     * <br>
     * DB（hh_hotel_url）から該当するURLを含む行データを取得し、URLを返します。<br>
     * del_flagが1のものに関して取得します。<br>
     * 行が存在しなかった場合は空文字を返します。<br>
     * 
     * @param id ホテルID
     * @param data_type サービスを表す識別子
     * @return 外部サービスのURL
     * @throws Exception
     */
    public String selectDeletedUrlRow(int id, int data_type) throws Exception
    {
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        return this.selectDeletedUrlRow( id, data_type, now_date );
    }

    /**
     * URL（を含む行データ）の取得処理<br>
     * <br>
     * DB（hh_hotel_url）から該当するURLを含む行データを取得し、URLを返します。<br>
     * del_flagが1のものに関して取得します。<br>
     * 行が存在しなかった場合は空文字を返します。<br>
     * 行のデータはインスタンス変数に格納されます。<br>
     * 
     * @param data_type サービスを表す識別子
     * @return 外部サービスのURL
     * @throws Exception
     */
    public String selectDeletedUrlRow(int data_type) throws Exception
    {
        return this.selectDeletedUrlRow( this.id, data_type );
    }

    /**
     * URLの挿入処理<br>
     * <br>
     * DB（hh_hotel_url）にURLのデータを挿入します。<br>
     * del_flagを1で書き込みます。<br>
     * 
     * @param id ホテルID
     * @param data_type サービスを表す識別子
     * @param url サービスのURL
     * @param now_date 更新日として記録する日付（YYYYMMDD）
     * @throws Exception
     */
    public void insertUrlWithDelFlag(int id, int data_type, String url, int now_date) throws Exception
    {
        // パラメータの代入
        this.id = id;
        this.url = url;
        this.dataType = data_type;
        this.delFlag = 1;
        this.startDate = now_date;
        this.endDate = 29991231;

        // 更新処理実行
        if ( this.insertData() == false )
        {
            throw new Exception( "URLの挿入処理に失敗しました。" );
        }
    }

    /**
     * URLの挿入処理<br>
     * <br>
     * DB（hh_hotel_url）にURLのデータを挿入します。<br>
     * del_flagを1で書き込みます。<br>
     * 
     * @param id ホテルID
     * @param data_type サービスを表す識別子
     * @param url サービスのURL
     * @throws Exception
     */
    public void insertUrlWithDelFlag(int id, int data_type, String url) throws Exception
    {
        // 更新日付として記録するための現在日時を取得
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        this.insertUrlWithDelFlag( id, data_type, url, now_date );
    }

    /**
     * URLの挿入処理<br>
     * <br>
     * DB（hh_hotel_url）にURLのデータを挿入します。<br>
     * del_flagを1で書き込みます。<br>
     * 
     * @param data_type サービスを表す識別子
     * @param url サービスのURL
     * @param now_date 更新日として記録する日付（YYYYMMDD）
     * @throws Exception
     */
    public void insertUrlWithDelFlag(int data_type, String url, int now_date) throws Exception
    {
        this.insertUrlWithDelFlag( this.id, data_type, url, now_date );
    }

    /**
     * URLの挿入処理<br>
     * <br>
     * DB（hh_hotel_url）にURLのデータを挿入します。<br>
     * del_flagを1で書き込みます。<br>
     * 
     * @param data_type サービスを表す識別子
     * @param url サービスのURL
     * @throws Exception
     */
    public void insertUrlWithDelFlag(int data_type, String url) throws Exception
    {
        this.insertUrlWithDelFlag( this.id, data_type, url );
    }

    /**
     * 対応する行のdel_flagの更新処理<br>
     * <br>
     * DB（hh_hotel_url）のデータを更新します。<br>
     * del_flagを0に書き変えます。<br>
     * 
     * @param id ホテルID
     * @param seq シーケンス番号
     * @param now_date 更新日として記録する日付（YYYYMMDD）
     * @throws Exception
     */
    public void updateToNotDeletedUrl(int id, int seq, int now_date) throws Exception
    {
        // クエリ生成
        String query = "";
        query += "UPDATE hh_hotel_url SET ";
        query += " del_flag = 0";
        query += ", start_date = " + now_date;
        query += " WHERE id = " + id;
        query += " AND seq = " + seq;

        // クエリ実行
        Connection connection = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.updateToNotDeletedUrl] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 対応する行のdel_flagの更新処理<br>
     * <br>
     * DB（hh_hotel_url）のデータを更新します。<br>
     * del_flagを0に書き変えます。<br>
     * 
     * @param id ホテルID
     * @param seq シーケンス番号
     * @throws Exception
     */
    public void updateToNotDeletedUrl(int id, int seq) throws Exception
    {
        // 更新日付として記録するための現在日時を取得
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        this.updateToNotDeletedUrl( id, seq, now_date );
    }

    /**
     * 対応する行のdel_flagの更新処理<br>
     * <br>
     * DB（hh_hotel_url）のデータを更新します。<br>
     * del_flagを0に書き変えます。<br>
     * 
     * @param seq シーケンス番号
     * @throws Exception
     */
    public void updateToNotDeletedUrl(int seq) throws Exception
    {
        this.updateToNotDeletedUrl( this.id, seq );
    }

    /**
     * 対応する行のdel_flagの更新処理<br>
     * <br>
     * DB（hh_hotel_url）のデータを更新します。<br>
     * del_flagを1に書き変えます。<br>
     * 
     * @param id ホテルID
     * @param seq シーケンス番号
     * @param now_date 更新日として記録する日付（YYYYMMDD）
     * @throws Exception
     */
    public void updateToDeletedUrl(int id, int seq, int now_date) throws Exception
    {
        // クエリ生成
        String query = "";
        query += "UPDATE hh_hotel_url SET ";
        query += " del_flag = 1";
        query += ", end_date = " + now_date;
        query += " WHERE id = " + id;
        query += " AND seq = " + seq;

        // クエリ実行
        Connection connection = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.updateToDeletedUrl] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * 対応する行のdel_flagの更新処理<br>
     * <br>
     * DB（hh_hotel_url）のデータを更新します。<br>
     * del_flagを1に書き変えます。<br>
     * 
     * @param id ホテルID
     * @param seq シーケンス番号
     * @throws Exception
     */
    public void updateToDeletedUrl(int id, int seq) throws Exception
    {
        // 更新日付として記録するための現在日時を取得
        int now_date = Integer.parseInt( DateEdit.getDate( 2 ) ); // YYYYMMDD

        this.updateToDeletedUrl( id, seq, now_date );
    }

    /**
     * 対応する行のdel_flagの更新処理<br>
     * <br>
     * DB（hh_hotel_url）のデータを更新します。<br>
     * del_flagを1に書き変えます。<br>
     * 
     * @param seq シーケンス番号
     * @throws Exception
     */
    public void updateToDeletedUrl(int seq) throws Exception
    {
        this.updateToDeletedUrl( this.id, seq );
    }

    /**
     * データの削除<br>
     * <br>
     * 指定した外部サービスにおいて、指定したシーケンス番号より大きいデータを削除します。<br>
     * 
     * @param id ホテルID
     * @param data_type サービスを表す識別子
     * @param seq シーケンス番号
     * @throws Exception
     */
    public void deleteOverSeqRows(int id, int data_type, int seq) throws Exception
    {
        // クエリ生成
        String query = "";
        query += "DELETE FROM hh_hotel_url ";
        query += " WHERE id = " + id;
        query += " AND data_type = " + data_type;
        query += " AND seq > " + seq;

        // クエリ実行
        Connection connection = null;
        PreparedStatement prestate = null;
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.executeUpdate();
        }
        catch ( Exception e )
        {
            Logging.error( "[ExternalServiceUrl.deleteOverSeqRows] Exception=" + e.toString() );
            throw e;
        }
        finally
        {
            DBConnection.releaseResources( prestate );
            DBConnection.releaseResources( connection );
        }
    }

    /**
     * データの削除<br>
     * <br>
     * 指定した外部サービスにおいて、指定したシーケンス番号より大きいデータを削除します。<br>
     * 
     * @param data_type サービスを表す識別子
     * @param seq シーケンス番号
     * @throws Exception
     */
    public void deleteOverSeqRows(int data_type, int seq) throws Exception
    {
        this.deleteOverSeqRows( this.id, data_type, seq );
    }
}
