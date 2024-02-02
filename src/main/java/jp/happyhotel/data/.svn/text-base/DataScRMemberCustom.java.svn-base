package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * メンバー別顧客登録情報(r_member_custom)取得クラス
 * 
 * @author K.Mitsuhashi
 * @version 1.00 2019/11/22
 */
public class DataScRMemberCustom implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = -3582553810189358856L;
    public static final String TABLE            = "sc.r_member_custom";
    private int                memberNo;                                // メンバーNO
    private int                id;                                      // ハピホテホテルID
    private String             securityCode;                            // セキュリティコード（ホスト登録）
    private String             customId;                                // メンバーID（ホスト登録）
    private String             hotelUserId;                             // ユーザーID（ホスト登録）
    private String             hotelPassword;                           // パスワード（ホスト登録）
    private int                birthday1;                               // 誕生日1（ホスト登録）
    private int                birthday2;                               // 誕生日2（ホスト登録）
    private int                memorial1;                               // 記念日1（ホスト登録）
    private int                memorial2;                               // 記念日2（ホスト登録）
    private String             nickname;                                // ニックネーム（ホスト登録用）
    private int                sex;                                     // 性別
    private String             name;                                    // 名前
    private String             nameKana;                                // 名前（カナ）
    private String             address1;                                // 住所1
    private String             address2;                                // 住所2
    private String             tel1;                                    // 電話1
    private int                registDate;                              // 登録日付(YYYYMMDD)
    private int                registTime;                              // 登録時刻(HHMMSS)
    private int                lastUpdate;                              // 最終更新日付(YYYYMMDD)
    private int                lastUptime;                              // 最終更新時刻(HHMMSS)
    private int                registStatus;                            // 0:ホストからOKのため仮登録、1:メンバ情報まで登録完了
    private int                delFlag;                                 // 1:削除
    private int                autoFlag;                                // 1:自動発行

    /**
     * データを初期化します。
     */
    public DataScRMemberCustom()
    {
        this.memberNo = 0;
        this.id = 0;
        this.securityCode = "";
        this.customId = "";
        this.hotelUserId = "";
        this.hotelPassword = "";
        this.birthday1 = 0;
        this.birthday2 = 0;
        this.memorial1 = 0;
        this.memorial2 = 0;
        this.nickname = "";
        this.sex = 0;
        this.name = "";
        this.nameKana = "";
        this.address1 = "";
        this.address2 = "";
        this.tel1 = "";
        this.registDate = 0;
        this.registTime = 0;
        this.lastUpdate = 0;
        this.lastUptime = 0;
        this.registStatus = 0;
        this.delFlag = 0;
        this.autoFlag = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getMemberNo()
    {
        return memberNo;
    }

    public String getSecurityCode()
    {
        return securityCode;
    }

    public String getCustomId()
    {
        return customId;
    }

    public String getHotelUserId()
    {
        return hotelUserId;
    }

    public String getHotelPassword()
    {
        return hotelPassword;
    }

    public int getBirthday1()
    {
        return birthday1;
    }

    public int getBirthday2()
    {
        return birthday2;
    }

    public int getMemorial1()
    {
        return memorial1;
    }

    public int getMemorial2()
    {
        return memorial2;
    }

    public String getNickname()
    {
        return nickname;
    }

    public int getSex()
    {
        return sex;
    }

    public String getName()
    {
        return name;
    }

    public String getNameKana()
    {
        return nameKana;
    }

    public String getAddress1()
    {
        return address1;
    }

    public String getAddress2()
    {
        return address2;
    }

    public String getTel1()
    {
        return tel1;
    }

    public int getRegistDate()
    {
        return registDate;
    }

    public int getRegistTime()
    {
        return registTime;
    }

    public int getLastUpdate()
    {
        return lastUpdate;
    }

    public int getLastUptime()
    {
        return lastUptime;
    }

    public int getRegistStatus()
    {
        return registStatus;
    }

    public int getDelFlag()
    {
        return delFlag;
    }

    public int getAutoFlag()
    {
        return autoFlag;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMemberNo(int memberNo)
    {
        this.memberNo = memberNo;
    }

    public void setSecurityCode(String securityCode)
    {
        this.securityCode = securityCode;
    }

    public void setCustomId(String customId)
    {
        this.customId = customId;
    }

    public void setHotelUserId(String hotelUserId)
    {
        this.hotelUserId = hotelUserId;
    }

    public void setHotelPassword(String hotelPassword)
    {
        this.hotelPassword = hotelPassword;
    }

    public void setBirthday1(int birthday1)
    {
        this.birthday1 = birthday1;
    }

    public void setBirthday2(int birthday2)
    {
        this.birthday2 = birthday2;
    }

    public void setMemorial1(int memorial1)
    {
        this.memorial1 = memorial1;
    }

    public void setMemorial2(int memorial2)
    {
        this.memorial2 = memorial2;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameKana(String nameKana)
    {
        this.nameKana = nameKana;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public void setTel1(String tel1)
    {
        this.tel1 = tel1;
    }

    public void setRegistDate(int registDate)
    {
        this.registDate = registDate;
    }

    public void setRegistTime(int registTime)
    {
        this.registTime = registTime;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    public int setRegistStatus()
    {
        return registStatus;
    }

    public void setRegistStatus(int registStatus)
    {
        this.registStatus = registStatus;
    }

    public void setLastUptime(int lastUptime)
    {
        this.lastUptime = lastUptime;
    }

    public void setDelFlag(int delFlag)
    {
        this.delFlag = delFlag;
    }

    public void setAutoFlag(int autoFlag)
    {
        this.autoFlag = autoFlag;
    }

    /****
     * メンバー別顧客登録情報(r_member_custom)取得
     * 
     * @param memberNo メンバーNO
     * @param id ハピホテホテルID
     * @see 登録完了データのみ
     * @return
     */
    public boolean getData(int memberNo, int id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM sc.r_member_custom WHERE member_no = ? AND id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, memberNo );
            prestate.setInt( 2, id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.memberNo = result.getInt( "member_no" );
                    this.id = result.getInt( "id" );
                    this.securityCode = result.getString( "security_code" );
                    this.customId = result.getString( "custom_id" );
                    this.hotelUserId = result.getString( "hotel_user_id" );
                    this.hotelPassword = result.getString( "hotel_password" );
                    this.birthday1 = result.getInt( "birthday1" );
                    this.birthday2 = result.getInt( "birthday2" );
                    this.memorial1 = result.getInt( "memorial1" );
                    this.memorial2 = result.getInt( "memorial2" );
                    this.nickname = result.getString( "nickname" );
                    this.sex = result.getInt( "sex" );
                    this.name = result.getString( "name" );
                    this.nameKana = result.getString( "name_kana" );
                    this.address1 = result.getString( "address1" );
                    this.address2 = result.getString( "address2" );
                    this.tel1 = result.getString( "tel1" );
                    this.registDate = result.getInt( "regist_date" );
                    this.registTime = result.getInt( "regist_time" );
                    this.lastUpdate = result.getInt( "last_update" );
                    this.lastUptime = result.getInt( "last_uptime" );
                    this.registStatus = result.getInt( "regist_status" );
                    this.delFlag = result.getInt( "del_flag" );
                    this.autoFlag = result.getInt( "auto_flag" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataScRMemberCustom.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * メンバー別顧客登録情報(sc.r_member_custom)設定
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
                this.memberNo = result.getInt( "member_no" );
                this.id = result.getInt( "id" );
                this.securityCode = result.getString( "security_code" );
                this.customId = result.getString( "custom_id" );
                this.hotelUserId = result.getString( "hotel_user_id" );
                this.hotelPassword = result.getString( "hotel_password" );
                this.birthday1 = result.getInt( "birthday1" );
                this.birthday2 = result.getInt( "birthday2" );
                this.memorial1 = result.getInt( "memorial1" );
                this.memorial2 = result.getInt( "memorial2" );
                this.nickname = result.getString( "nickname" );
                this.sex = result.getInt( "sex" );
                this.name = result.getString( "name" );
                this.nameKana = result.getString( "name_kana" );
                this.address1 = result.getString( "address1" );
                this.address2 = result.getString( "address2" );
                this.tel1 = result.getString( "tel1" );
                this.registDate = result.getInt( "regist_date" );
                this.registTime = result.getInt( "regist_time" );
                this.lastUpdate = result.getInt( "last_update" );
                this.lastUptime = result.getInt( "last_uptime" );
                this.registStatus = result.getInt( "regist_status" );
                this.delFlag = result.getInt( "del_flag" );
                this.autoFlag = result.getInt( "auto_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataScRMemberCustom.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * メンバー別顧客登録情報(sc.r_member_custom)挿入
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

        query = "INSERT sc.r_member_custom SET ";
        query += " member_no=?";
        query += ", id=?";
        query += ", security_code=?";
        query += ", custom_id=?";
        query += ", hotel_user_id=?";
        query += ", hotel_password=?";
        query += ", birthday1=?";
        query += ", birthday2=?";
        query += ", memorial1=?";
        query += ", memorial2=?";
        query += ", nickname=?";
        query += ", sex=?";
        query += ", name=?";
        query += ", name_kana=?";
        query += ", address1=?";
        query += ", address2=?";
        query += ", tel1=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", regist_status=?";
        query += ", del_flag=?";
        query += ", auto_flag=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // 更新対象の値をセットする
            prestate.setInt( i++, this.memberNo );
            prestate.setInt( i++, this.id );
            prestate.setString( i++, this.securityCode );
            prestate.setString( i++, this.customId );
            prestate.setString( i++, this.hotelUserId );
            prestate.setString( i++, this.hotelPassword );
            prestate.setInt( i++, this.birthday1 );
            prestate.setInt( i++, this.birthday2 );
            prestate.setInt( i++, this.memorial1 );
            prestate.setInt( i++, this.memorial2 );
            prestate.setString( i++, this.nickname );
            prestate.setInt( i++, this.sex );
            prestate.setString( i++, this.name );
            prestate.setString( i++, this.nameKana );
            prestate.setString( i++, this.address1 );
            prestate.setString( i++, this.address2 );
            prestate.setString( i++, this.tel1 );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.registStatus );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.autoFlag );

            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[DataScRMemberCustom.insertData] Exception=" + e.toString() );
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
     * メンバー別顧客登録情報(sc.r_member_custom)更新
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @param id ハピホテホテルID
     * @param userId ユーザーID
     * @return
     */
    public boolean updateData(int id, String userId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE sc.r_member_custom SET ";
        query += " security_code=?";
        query += ", custom_id=?";
        query += ", hotel_user_id=?";
        query += ", hotel_password=?";
        query += ", birthday1=?";
        query += ", birthday2=?";
        query += ", memorial1=?";
        query += ", memorial2=?";
        query += ", nickname=?";
        query += ", sex=?";
        query += ", name=?";
        query += ", name_kana=?";
        query += ", address1=?";
        query += ", address2=?";
        query += ", tel1=?";
        query += ", regist_date=?";
        query += ", regist_time=?";
        query += ", last_update=?";
        query += ", last_uptime=?";
        query += ", regist_status=?";
        query += ", del_flag=?";
        query += ", auto_flag=?";
        query += " WHERE member_no=? AND id=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // 更新対象の値をセットする
            prestate.setString( i++, this.securityCode );
            prestate.setString( i++, this.customId );
            prestate.setString( i++, this.hotelUserId );
            prestate.setString( i++, this.hotelPassword );
            prestate.setInt( i++, this.birthday1 );
            prestate.setInt( i++, this.birthday2 );
            prestate.setInt( i++, this.memorial1 );
            prestate.setInt( i++, this.memorial2 );
            prestate.setString( i++, this.nickname );
            prestate.setInt( i++, this.sex );
            prestate.setString( i++, this.name );
            prestate.setString( i++, this.nameKana );
            prestate.setString( i++, this.address1 );
            prestate.setString( i++, this.address2 );
            prestate.setString( i++, this.tel1 );
            prestate.setInt( i++, this.registDate );
            prestate.setInt( i++, this.registTime );
            prestate.setInt( i++, this.lastUpdate );
            prestate.setInt( i++, this.lastUptime );
            prestate.setInt( i++, this.registStatus );
            prestate.setInt( i++, this.delFlag );
            prestate.setInt( i++, this.autoFlag );
            prestate.setInt( i++, this.memberNo );
            prestate.setInt( i++, this.id );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }

        }
        catch ( Exception e )
        {
            Logging.error( "[DataScRMemberCustom.updateData] Exception=" + e.toString() );
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
