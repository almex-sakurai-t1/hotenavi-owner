/*
 * @(#)DataMasterChain.java 1.00 2007/07/18 Copyright (C) ALMEX Inc. 2007 チェーン店マスタ取得クラス
 */
package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.DBSync;
import jp.happyhotel.common.Logging;

/**
 * チェーン店マスタ取得クラス
 * 
 * @author S.Shiiya
 * @version 1.00 2007/07/18
 * @version 1.1 2007/11/29
 */
public class DataMasterChain implements Serializable
{
    /**
	 *
	 */
    private static final long serialVersionUID = -5784065892056994397L;

    private int               groupId;
    private String            groupHotenavi;
    private String            name;
    private String            nameKana;
    private String            pr;
    private byte[]            pictureJpg;
    private byte[]            pictureGif;
    private byte[]            picturePng;
    private int               dispFlag;
    private String            nameCorporation;
    private String            nameCorporationKana;
    private String            zipCode;
    private String            prefName;
    private String            address1;
    private String            address2;
    private String            tel1;
    private String            tel2;
    private String            fax;
    private String            presidentLast;
    private String            presidentFirst;
    private String            presidentKanaLast;
    private String            presidentKanaFirst;
    private String            presidentTel;
    private String            presidentMail;
    private String            chargeLast;
    private String            chargeFirst;
    private String            chargeKanaLast;
    private String            chargeKanaFirst;
    private String            chargeTel;
    private String            chargeMail;
    private String            url;
    private String            extCode;
    private int               lastUpdate;
    private int               lastUptime;
    private String            memo;
    private int               scReserveRate;

    /**
     * データを初期化します。
     */
    public DataMasterChain()
    {
        groupId = 0;
        groupHotenavi = "";
        name = "";
        nameKana = "";
        pr = "";
        pictureJpg = new byte[0];
        pictureGif = new byte[0];
        picturePng = new byte[0];
        dispFlag = 0;
        nameCorporation = "";
        nameCorporationKana = "";
        zipCode = "";
        prefName = "";
        address1 = "";
        address2 = "";
        tel1 = "";
        tel2 = "";
        fax = "";
        presidentLast = "";
        presidentFirst = "";
        presidentKanaLast = "";
        presidentKanaFirst = "";
        presidentTel = "";
        presidentMail = "";
        chargeLast = "";
        chargeFirst = "";
        chargeKanaLast = "";
        chargeKanaFirst = "";
        chargeTel = "";
        chargeMail = "";
        url = "";
        extCode = "";
        lastUpdate = 0;
        lastUptime = 0;
        memo = "";
        scReserveRate = 0;
    }

    public int getGroupId()
    {
        return groupId;
    }

    public void setGroupId(int groupId)
    {
        this.groupId = groupId;
    }

    public String getGroupHotenavi()
    {
        return groupHotenavi;
    }

    public void setGroupHotenavi(String groupHotenavi)
    {
        this.groupHotenavi = groupHotenavi;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public String getPr()
    {
        return pr;
    }

    public void setPr(String pr)
    {
        this.pr = pr;
    }

    public byte[] getPictureJpg()
    {
        return pictureJpg;
    }

    public void setPictureJpg(byte[] pictureJpg)
    {
        this.pictureJpg = pictureJpg;
    }

    public byte[] getPictureGif()
    {
        return pictureGif;
    }

    public void setPictureGif(byte[] pictureGif)
    {
        this.pictureGif = pictureGif;
    }

    public byte[] getPicturePng()
    {
        return picturePng;
    }

    public void setPicturePng(byte[] picturePng)
    {
        this.picturePng = picturePng;
    }

    public int getDispFlag()
    {
        return dispFlag;
    }

    public void setDispFlag(int dispFlag)
    {
        this.dispFlag = dispFlag;
    }

    public String getNameCorporation()
    {
        return nameCorporation;
    }

    public void setNameCorporation(String nameCorporation)
    {
        this.nameCorporation = nameCorporation;
    }

    public String getNameCorporationKana()
    {
        return nameCorporationKana;
    }

    public void setNameCorporationKana(String nameCorporationKana)
    {
        this.nameCorporationKana = nameCorporationKana;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public String getPrefName()
    {
        return prefName;
    }

    public void setPrefName(String prefName)
    {
        this.prefName = prefName;
    }

    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getTel1()
    {
        return tel1;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public String getTel2()
    {
        return tel2;
    }

    public void setTel2(String tel2)
    {
        this.tel2 = tel2;
    }

    public String getFax()
    {
        return fax;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public String getPresidentLast()
    {
        return presidentLast;
    }

    public void setPresidentLast(String presidentLast)
    {
        this.presidentLast = presidentLast;
    }

    public String getPresidentFirst()
    {
        return presidentFirst;
    }

    public void setPresidentFirst(String presidentFirst)
    {
        this.presidentFirst = presidentFirst;
    }

    public String getPresidentKanaLast()
    {
        return presidentKanaLast;
    }

    public void setPresidentKanaLast(String presidentKanaLast)
    {
        this.presidentKanaLast = presidentKanaLast;
    }

    public String getPresidentKanaFirst()
    {
        return presidentKanaFirst;
    }

    public void setPresidentKanaFirst(String presidentKanaFirst)
    {
        this.presidentKanaFirst = presidentKanaFirst;
    }

    public String getPresidentTel()
    {
        return presidentTel;
    }

    public void setPresidentTel(String presidentTel)
    {
        this.presidentTel = presidentTel;
    }

    public String getPresidentMail()
    {
        return presidentMail;
    }

    public void setPresidentMail(String presidentMail)
    {
        this.presidentMail = presidentMail;
    }

    public String getChargeLast()
    {
        return chargeLast;
    }

    public void setChargeLast(String chargeLast)
    {
        this.chargeLast = chargeLast;
    }

    public String getChargeFirst()
    {
        return chargeFirst;
    }

    public void setChargeFirst(String chargeFirst)
    {
        this.chargeFirst = chargeFirst;
    }

    public String getChargeKanaLast()
    {
        return chargeKanaLast;
    }

    public void setChargeKanaLast(String chargeKanaLast)
    {
        this.chargeKanaLast = chargeKanaLast;
    }

    public String getChargeKanaFirst()
    {
        return chargeKanaFirst;
    }

    public void setChargeKanaFirst(String chargeKanaFirst)
    {
        this.chargeKanaFirst = chargeKanaFirst;
    }

    public String getChargeTel()
    {
        return chargeTel;
    }

    public void setChargeTel(String chargeTel)
    {
        this.chargeTel = chargeTel;
    }

    public String getChargeMail()
    {
        return chargeMail;
    }

    public void setChargeMail(String chargeMail)
    {
        this.chargeMail = chargeMail;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getExtCode()
    {
        return extCode;
    }

    public void setExtCode(String extCode)
    {
        this.extCode = extCode;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public int getScReserveRate()
    {
        return scReserveRate;
    }

    public void setScReserveRate(int scReserveRate)
    {
        this.scReserveRate = scReserveRate;
    }

    /**
     * チェーン店マスタ取得
     * 
     * @param groupId グループID
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(int groupId)
    {
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        boolean ret = false;
        query = "SELECT * FROM hh_master_chain WHERE group_id = ?";

        try
        {
            connection = DBConnection.getReadOnlyConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, groupId );
            result = prestate.executeQuery();

            if ( result.next() )
            {
                ret = setData( result );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterChain.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return ret;
    }

    /**
     * チェーン店マスタ設定
     * 
     * @param result チェーン店マスタレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.groupId = result.getInt( "group_id" );
                this.groupHotenavi = result.getString( "group_hotenavi" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.pr = result.getString( "pr" );
                this.pictureJpg = result.getBytes( "picture_jpg" );
                this.pictureGif = result.getBytes( "picture_gif" );
                this.picturePng = result.getBytes( "picture_png" );
                this.dispFlag = result.getInt( "disp_flag" );
                this.nameCorporation = result.getString( "name_corporation" );
                this.nameCorporationKana = result.getString( "name_corporation_kana" );
                this.zipCode = result.getString( "zip_code" );
                this.prefName = result.getString( "pref_name" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.tel1 = result.getString( "tel1" );
                this.tel2 = result.getString( "tel2" );
                this.fax = result.getString( "fax" );
                this.presidentLast = result.getString( "president_last" );
                this.presidentFirst = result.getString( "president_first" );
                this.presidentKanaLast = result.getString( "president_kana_last" );
                this.presidentKanaFirst = result.getString( "president_kana_first" );
                this.presidentTel = result.getString( "president_tel" );
                this.presidentMail = result.getString( "president_mail" );
                this.chargeLast = result.getString( "charge_last" );
                this.chargeFirst = result.getString( "charge_first" );
                this.chargeKanaLast = result.getString( "charge_kana_last" );
                this.chargeKanaFirst = result.getString( "charge_kana_first" );
                this.chargeTel = result.getString( "charge_tel" );
                this.chargeMail = result.getString( "charge_mail" );
                this.url = result.getString( "url" );
                this.extCode = result.getString( "ext_code" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.memo = result.getString( "memo" );
                this.scReserveRate = result.getInt( "sc_reserve_rate" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterChain.setData] Exception=" + e.toString() );
        }

        return(true);
    }

    /**
     * チェーン店データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData()
    {
        return insertData( false );
    }

    /**
     * チェーン店データ追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertData(boolean isStg)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "INSERT hh_master_chain SET ";
        query = query + " group_id = ?,";
        query = query + " group_hotenavi = ?,";
        query = query + " name = ?,";
        query = query + " name_kana = ?,";
        query = query + " pr = ?,";
        query = query + " picture_jpg = ?,";
        query = query + " picture_gif = ?,";
        query = query + " picture_png = ?,";
        query = query + " disp_flag = ?,";
        query = query + " name_corporation = ?,";
        query = query + " name_corporation_kana = ?,";
        query = query + " zip_code = ?,";
        query = query + " pref_name = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " fax = ?,";
        query = query + " president_last = ?,";
        query = query + " president_first = ?,";
        query = query + " president_kana_last = ?,";
        query = query + " president_kana_first = ?,";
        query = query + " president_tel = ?,";
        query = query + " president_mail = ?,";
        query = query + " charge_last = ?,";
        query = query + " charge_first = ?,";
        query = query + " charge_kana_last = ?,";
        query = query + " charge_kana_first = ?,";
        query = query + " charge_tel = ?,";
        query = query + " charge_mail = ?,";
        query = query + " url = ?,";
        query = query + " ext_code = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " memo = ?,";
        query = query + " sc_reserve_rate = ? ";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            int a = 1;
            prestate.setInt( a++, this.groupId );
            prestate.setString( a++, this.groupHotenavi );
            prestate.setString( a++, this.name );
            prestate.setString( a++, this.nameKana );
            prestate.setString( a++, this.pr );
            prestate.setBytes( a++, this.pictureJpg );
            prestate.setBytes( a++, this.pictureGif );
            prestate.setBytes( a++, this.picturePng );
            prestate.setInt( a++, this.dispFlag );
            prestate.setString( a++, this.nameCorporation );
            prestate.setString( a++, this.nameCorporationKana );
            prestate.setString( a++, this.zipCode );
            prestate.setString( a++, this.prefName );
            prestate.setString( a++, this.address1 );
            prestate.setString( a++, this.address2 );
            prestate.setString( a++, this.tel1 );
            prestate.setString( a++, this.tel2 );
            prestate.setString( a++, this.fax );
            prestate.setString( a++, this.presidentLast );
            prestate.setString( a++, this.presidentFirst );
            prestate.setString( a++, this.presidentKanaLast );
            prestate.setString( a++, this.presidentKanaFirst );
            prestate.setString( a++, this.presidentTel );
            prestate.setString( a++, this.presidentMail );
            prestate.setString( a++, this.chargeLast );
            prestate.setString( a++, this.chargeFirst );
            prestate.setString( a++, this.chargeKanaLast );
            prestate.setString( a++, this.chargeKanaFirst );
            prestate.setString( a++, this.chargeTel );
            prestate.setString( a++, this.chargeMail );
            prestate.setString( a++, this.url );
            prestate.setString( a++, this.extCode );
            prestate.setInt( a++, this.lastUpdate );
            prestate.setInt( a++, this.lastUptime );
            prestate.setString( a++, this.memo );
            prestate.setInt( a++, this.scReserveRate );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                // PubSubデータ同期
                DBSync.publish( prestate, true, isStg );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterChain.insertData] Exception=" + e.toString() );
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
     * ホテル部屋情報データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param groupId
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int groupId)
    {
        return updateData( groupId, false );
    }

    /**
     * チェーン店データ変更
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param sponsorCode
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean updateData(int groupId, boolean isStg)
    {
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "UPDATE hh_master_chain SET ";
        query = query + " group_hotenavi = ?,";
        query = query + " name = ?,";
        query = query + " name_kana = ?,";
        query = query + " pr = ?,";
        query = query + " picture_jpg = ?,";
        query = query + " picture_gif = ?,";
        query = query + " picture_png = ?,";
        query = query + " disp_flag = ?,";
        query = query + " name_corporation = ?,";
        query = query + " name_corporation_kana = ?,";
        query = query + " zip_code = ?,";
        query = query + " pref_name = ?,";
        query = query + " address1 = ?,";
        query = query + " address2 = ?,";
        query = query + " tel1 = ?,";
        query = query + " tel2 = ?,";
        query = query + " fax = ?,";
        query = query + " president_last = ?,";
        query = query + " president_first = ?,";
        query = query + " president_kana_last = ?,";
        query = query + " president_kana_first = ?,";
        query = query + " president_tel = ?,";
        query = query + " president_mail = ?,";
        query = query + " charge_last = ?,";
        query = query + " charge_first = ?,";
        query = query + " charge_kana_last = ?,";
        query = query + " charge_kana_first = ?,";
        query = query + " charge_tel = ?,";
        query = query + " charge_mail = ?,";
        query = query + " url = ?,";
        query = query + " ext_code = ?,";
        query = query + " last_update = ?,";
        query = query + " last_uptime = ?,";
        query = query + " memo = ?, ";
        query = query + " sc_reserve_rate = ?";
        query = query + " WHERE group_id = ?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            int a = 1;
            prestate.setString( a++, this.groupHotenavi );
            prestate.setString( a++, this.name );
            prestate.setString( a++, this.nameKana );
            prestate.setString( a++, this.pr );
            prestate.setBytes( a++, this.pictureJpg );
            prestate.setBytes( a++, this.pictureGif );
            prestate.setBytes( a++, this.picturePng );
            prestate.setInt( a++, this.dispFlag );
            prestate.setString( a++, this.nameCorporation );
            prestate.setString( a++, this.nameCorporationKana );
            prestate.setString( a++, this.zipCode );
            prestate.setString( a++, this.prefName );
            prestate.setString( a++, this.address1 );
            prestate.setString( a++, this.address2 );
            prestate.setString( a++, this.tel1 );
            prestate.setString( a++, this.tel2 );
            prestate.setString( a++, this.fax );
            prestate.setString( a++, this.presidentLast );
            prestate.setString( a++, this.presidentFirst );
            prestate.setString( a++, this.presidentKanaLast );
            prestate.setString( a++, this.presidentKanaFirst );
            prestate.setString( a++, this.presidentTel );
            prestate.setString( a++, this.presidentMail );
            prestate.setString( a++, this.chargeLast );
            prestate.setString( a++, this.chargeFirst );
            prestate.setString( a++, this.chargeKanaLast );
            prestate.setString( a++, this.chargeKanaFirst );
            prestate.setString( a++, this.chargeTel );
            prestate.setString( a++, this.chargeMail );
            prestate.setString( a++, this.url );
            prestate.setString( a++, this.extCode );
            prestate.setInt( a++, this.lastUpdate );
            prestate.setInt( a++, this.lastUptime );
            prestate.setString( a++, this.memo );
            prestate.setInt( a++, this.scReserveRate );
            prestate.setInt( a++, groupId );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                // sql文取得
                String sql = prestate.toString().split( ":", 2 )[1];

                // schema追加
                sql = sql.replace( "UPDATE ", "UPDATE hotenavi." );

                // updateをreplace文に変える
                sql = DBSync.updateToReplaceSQL( sql );

                // PubSubデータ同期
                DBSync.publish( sql, isStg );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataMasterChain.updateData] Exception=" + e.toString() );
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
