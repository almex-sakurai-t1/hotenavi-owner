/*
 * @(#)HotelAuth.java 1.00
 * 2011/01/24 Copyright (C) ALMEX Inc. 2011
 * ホテルページビュー情報取得クラス
 */
package jp.happyhotel.hotel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.CheckString;
import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DateEdit;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.RandomString;
import jp.happyhotel.data.DataHotelAuth;
import jp.happyhotel.data.DataHotelBasic;

/**
 * ホテル認証クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2011/01/24
 */
public class HotelAuth implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID          = 364969292829269423L;
    DataHotelAuth[]           hotelAuth;
    int                       hotelCount;
    static final int          TEMP_KEY_NUM              = 6;
    static final int          STATUS_REGIST_STILL       = 0;
    static final int          STATUS_REGIST_FINISH      = 1;
    static final int          STATUS_REGIST_UNAVAILABLE = 2;

    /**
     * データを初期化します。
     */
    public HotelAuth()
    {
        hotelCount = 0;
        hotelAuth = null;
    }

    public DataHotelAuth[] getHotelAuth()
    {
        return hotelAuth;
    }

    public int getHotelCount()
    {
        return hotelCount;
    }

    public void setHotelPv(DataHotelAuth[] hotelAuth)
    {
        this.hotelAuth = hotelAuth;
    }

    public void setHotelCount(int hotelCount)
    {
        this.hotelCount = hotelCount;
    }

    /**
     * ホテル認証データ一覧取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getAuthList(int hotelId)
    {
        boolean ret;
        int i;
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        query = "SELECT hh_hotel_auth.* FROM hh_hotel_auth, hh_hotel_basic ";
        query += " WHERE hh_hotel_auth.id = hh_hotel_basic.id";
        // 本番環境時に変更
        // query += " WHERE hh_hotel_auth.id = hh_hotel_basic.id AND hh_hotel_basic.rank >= 3";
        query += " AND hh_hotel_auth.id = ?";
        query += " ORDER BY hh_hotel_auth.regist_date Desc, hh_hotel_auth.regist_time DESC";

        count = 0;

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                // レコード件数取得
                if ( result.last() != false )
                {
                    this.hotelCount = result.getRow();
                }

                // クラスの配列を用意し、初期化する。
                this.hotelAuth = new DataHotelAuth[this.hotelCount];
                for( i = 0 ; i < hotelCount ; i++ )
                {
                    this.hotelAuth[i] = new DataHotelAuth();
                }

                result.beforeFirst();
                while( result.next() != false )
                {
                    // ホテル認証の取得
                    this.hotelAuth[count++].setData( result );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelAuth.getAuthList] Exception=" + e.toString() );
            this.hotelCount = 0;
            return(false);
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }

        if ( this.hotelCount > 0 )
        {
            ret = true;
        }
        else
        {
            ret = false;
        }
        return(ret);
    }

    /**
     * MACアドレス登録
     * 
     * @param licenceKey ライセンスキー
     * @param macAddr MACアドレス
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean registMacAddr(String licenceKey, String macAddr, String version)
    {
        int count;
        boolean ret = false;
        DataHotelAuth dha;
        dha = new DataHotelAuth();

        // nullや空白、桁数が違っていたらエラー
        if ( (licenceKey == null) || (licenceKey.compareTo( "" ) == 0) || (licenceKey.length() != TEMP_KEY_NUM) )
        {
            return false;
        }
        if ( (version == null) || (version.compareTo( "" ) == 0) )
        {
            version = "";
        }

        // 有効なデータがあれば無条件でTRUE
        ret = dha.getValidData( licenceKey, macAddr );
        Logging.info( "registMacAddr:有効データ取得結果:" + ret + ", ライセンス:" + licenceKey + ", MACアドレス:" + macAddr );
        if ( ret != false )
        {
            // 更新日、更新時刻を更新
            dha.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dha.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );

            // バージョン情報が入っていたら登録
            if ( version.equals( "" ) == false )
            {
                dha.setVersion( version );
            }
            // データを更新
            ret = dha.updateData( licenceKey );
            Logging.info( "registMacAddr:データ更新結果:" + ret + ret + ", ライセンス:" + licenceKey + ", MACアドレス:" + macAddr );
        }
        // ない場合は重複チェックの確認
        else
        {
            // MACアドレスの重複状態を確認（2は無効なので0または1のみ）
            count = this.getSameMacAddrCount( macAddr );

            Logging.info( "registMacAddr:MACアドレス重複数:" + count + ret + ", ライセンス:" + licenceKey + ", MACアドレス:" + macAddr );

            // 重複チェックなし
            if ( count == 0 )
            {
                ret = dha.getData( licenceKey );
                if ( ret != false )
                {
                    // ホテルIDとライセンスキーが登録されており、登録状況が無効以外だったらOK
                    if ( dha.getId() > 0 && dha.getLicenceKey().compareTo( "" ) != 0 && dha.getRegistStatus() < 2 )
                    {
                        // マックアドレスが空白だったら登録
                        if ( dha.getMacAddr().compareTo( "" ) == 0 )
                        {
                            dha.setMacAddr( macAddr );
                            dha.setRegistStatus( STATUS_REGIST_FINISH );
                            dha.setAuthDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            dha.setAuthTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            dha.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            dha.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            if ( version.equals( "" ) == false )
                            {
                                dha.setVersion( version );
                            }

                            // データを更新
                            ret = dha.updateData( licenceKey );
                            Logging.info( "registMacAddr:データ登録結果:" + ret + ret + ", ライセンス:" + licenceKey + ", MACアドレス:" + macAddr );
                        }
                        // マックアドレスが一致したらtrue
                        else if ( dha.getMacAddr().compareTo( macAddr ) == 0 )
                        {
                            // 更新日、更新時刻を更新
                            dha.setLastUpdate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                            dha.setLastUptime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                            if ( version.equals( "" ) == false )
                            {
                                dha.setVersion( version );
                            }

                            ret = true;
                            // データを更新
                            ret = dha.updateData( licenceKey );
                            Logging.info( "registMacAddr:MACアドレス一致、更新日の更新結果:" + ret + ret + ", ライセンス:" + licenceKey + ", MACアドレス:" + macAddr );
                        }
                        // それ以外はfalse
                        else
                        {
                            Logging.info( "registMacAddr:登録MACアドレス:" + dha.getMacAddr() + ", ライセンス:" + licenceKey + ", MACアドレス:" + macAddr );
                            ret = false;
                        }
                    }
                    else
                    {
                        Logging.info( "registMacAddr:登録状況が無効" + ret + ", ライセンス:" + licenceKey + ", MACアドレス:" + macAddr );
                        ret = false;
                    }
                }
            }
            else
            {
                Logging.info( "registMacAddr:MACアドレス重複あり" + ret + ", ライセンス:" + licenceKey + ", MACアドレス:" + macAddr );
                ret = false;
            }
        }
        return(ret);
    }

    /**
     * ライセンスキー登録
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean registLicenceKey(int hotelId)
    {
        boolean ret;
        String licenceKey;
        DataHotelAuth dha;
        DataHotelBasic dhb;

        ret = false;
        licenceKey = "";
        dha = new DataHotelAuth();
        dhb = new DataHotelBasic();

        dhb.getData( hotelId );
        if ( dhb.getId() > 0 )
        {
            // ライセンスキーを発行
            licenceKey = this.makeLicenceKey();
            // ライセンスキーが空白以外で桁数があっていれば登録
            if ( licenceKey.compareTo( "" ) != 0 && licenceKey.length() == TEMP_KEY_NUM )
            {
                dha.setId( dhb.getId() );
                dha.setLicenceKey( licenceKey );
                dha.setMacAddr( "" );
                dha.setRegistStatus( STATUS_REGIST_STILL );
                dha.setRegistDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dha.setRegistTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                ret = dha.insertData();
            }
            else
            {
                ret = false;
            }
        }
        else
        {
            ret = false;
        }

        return(ret);
    }

    /**
     * ライセンスキー作成
     * 
     * @return 処理結果()
     */
    public String makeLicenceKey()
    {
        int count;
        String tempKey;
        count = 0;
        tempKey = "";
        // 問題のないライセンスキーが登録されたらbreak
        while( true )
        {
            tempKey = RandomString.getLicenceKey( TEMP_KEY_NUM );
            // ユーザーID数値チェック(数字以外が含まれていればOK)
            if ( CheckString.numCheck( tempKey ) == false )
            {
                count = this.getSameKeyCount( tempKey );
                // 同じIDが登録されていなければOK
                if ( count == 0 )
                {
                    break;
                }
                else if ( count < 0 )
                {
                    tempKey = "";
                    break;
                }
            }
        }
        return(tempKey);
    }

    /**
     * ライセンスキー重複チェック
     * 
     * @param tempKey 一時発行キー
     * @return 処理結果(TRUE:重複あり,FALSE:重複なし)
     */
    public int getSameKeyCount(String tempKey)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        count = 0;
        query = "SELECT COUNT(*) FROM hh_hotel_auth WHERE licence_key = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, tempKey );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                    // もしデータがあったらtrue
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelAuth.checkDuplicate()] Exception:" + e.toString() );
            count = -1;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * 最新データ取得
     * 
     * @param hotelId ホテルID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getLatestData(int hotelId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;
        query = "SELECT * FROM hh_hotel_auth WHERE id = ?";
        query += " ORDER BY regist_date DESC, regist_time DESC";
        query += " Limit 0,1";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, hotelId );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.hotelCount = 1;
                    this.hotelAuth = new DataHotelAuth[1];
                    this.hotelAuth[0] = new DataHotelAuth();
                    this.hotelAuth[0].setData( result );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelAuth.getLatestData()] Exception:" + e.toString() );
            ret = false;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * MACアドレス重複チェック（regist_status=2は無効のため論外）
     * 
     * @param macAddr マックアドレス
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public int getSameMacAddrCount(String macAddr)
    {
        int count;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        count = 0;
        query = "SELECT COUNT(*) FROM hh_hotel_auth WHERE mac_addr = ?";
        query += " AND regist_date > 0";
        query += " AND regist_time > 0";
        query += " AND regist_status = 1";
        query += " ORDER BY regist_date DESC, regist_time DESC";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, macAddr );

            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    count = result.getInt( 1 );
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HotelAuth.getSameMacAddrCount()] Exception:" + e.toString() );
            count = -1;
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(count);
    }

    /**
     * ライセンスキー無効処理
     * 
     * @param hotelId ホテルID
     * @param licencekey ライセンスキー
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean unavailableLicence(int hotelId, String licenceKey)
    {
        boolean ret = false;
        boolean retAuth = false;
        DataHotelAuth dha;
        DataHotelBasic dhb;

        dha = new DataHotelAuth();
        dhb = new DataHotelBasic();

        ret = dhb.getData( hotelId );
        retAuth = dha.getData( licenceKey );
        if ( ret != false && retAuth )
        {
            if ( dhb.getId() == dha.getId() )
            {
                dha.setRegistStatus( STATUS_REGIST_UNAVAILABLE );
                dha.setAuthDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dha.setAuthTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                ret = dha.updateData( licenceKey );
            }
            else
            {
                ret = false;
            }
        }
        else
        {
            ret = false;
        }
        return(ret);
    }
}
