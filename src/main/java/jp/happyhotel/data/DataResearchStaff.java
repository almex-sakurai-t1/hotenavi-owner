package jp.happyhotel.data;

/*
 * ホテル認証データ
 * generator Version 1.0.0 release 2011/01/12
 * generated Date Wed Jan 12 16:30:53 JST 2011
 */
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;

/**
 * 社員マスタ
 * 
 * @author T.Sakurai
 */
public class DataResearchStaff implements Serializable
{
    private static final long serialVersionUID = -1004988843280584964L;
    private String            staff_id;
    private String            name_last;
    private String            name_first;
    private String            name_kana_last;
    private String            name_kana_first;
    private String            section_id;
    private String            section_name;
    private String            mail1;
    private String            mail2;
    private String            phs;
    private String            mail_mobile;
    private String            machineid;
    private int               kind;
    private int               last_update;
    private int               last_uptime;
    private int               del_date;
    private int               del_flag;
    private int               passwd_update;
    private String            custom_id;

    public DataResearchStaff()
    {
        staff_id = "";
        name_last = "";
        name_first = "";
        name_kana_last = "";
        name_kana_first = "";
        section_id = "";
        section_name = "";
        mail1 = "";
        mail2 = "";
        phs = "";
        mail_mobile = "";
        machineid = "";
        kind = 0;
        last_update = 0;
        last_uptime = 0;
        del_date = 0;
        del_flag = 0;
        passwd_update = 0;
        custom_id = "";
    }

    public String getStaffId()
    {
        return this.staff_id;
    }

    public String getNameLast()
    {
        return this.name_last;
    }

    public String getNameFirst()
    {
        return this.name_first;
    }

    public String getNameKanaLast()
    {
        return this.name_kana_last;
    }

    public String getNameKanaFirst()
    {
        return this.name_kana_first;
    }

    public String getSectionId()
    {
        return this.section_id;
    }

    public String getSectionName()
    {
        return this.section_name;
    }

    public String getMail1()
    {
        return this.mail1;
    }

    public String getMail2()
    {
        return this.mail2;
    }

    public String getPhs()
    {
        return this.phs;
    }

    public String getMailMobile()
    {
        return this.mail_mobile;
    }

    public String getMachineid()
    {
        return this.machineid;
    }

    public int getKind()
    {
        return this.kind;
    }

    public int getLastUpdate()
    {
        return this.last_update;
    }

    public int getLastUptime()
    {
        return this.last_uptime;
    }

    public int getDelDate()
    {
        return this.del_date;
    }

    public int getDelFlag()
    {
        return this.del_flag;
    }

    public int getPasswdUpdate()
    {
        return this.passwd_update;
    }

    public String getCustomId()
    {
        return this.custom_id;
    }

    public void setStaffId(String staffId)
    {
        this.staff_id = staffId;
    }

    public void setNameLast(String nameLast)
    {
        this.name_last = nameLast;
    }

    public void setNameFirst(String nameFirst)
    {
        this.name_first = nameFirst;
    }

    public void setNameKanaLast(String nameKanaLast)
    {
        this.name_kana_last = nameKanaLast;
    }

    public void setNameKanaFirst(String nameKanaFirst)
    {
        this.name_kana_first = nameKanaFirst;
    }

    public void setSectionId(String sectionId)
    {
        this.section_id = sectionId;
    }

    public void setSectionName(String sectionName)
    {
        this.section_name = sectionName;
    }

    public void setMail1(String mail1)
    {
        this.mail1 = mail1;
    }

    public void setMail2(String mail2)
    {
        this.mail2 = mail2;
    }

    public void setPhs(String phs)
    {
        this.phs = phs;
    }

    public void setMailMobile(String mailMobile)
    {
        this.mail_mobile = mailMobile;
    }

    public void setMachineid(String machineid)
    {
        this.machineid = machineid;
    }

    public void setKind(int kind)
    {
        this.kind = kind;
    }

    public void setLastUpdate(int lastUpdate)
    {
        this.last_update = lastUpdate;
    }

    public void setLastUptime(int lastUptime)
    {
        this.last_uptime = lastUptime;
    }

    public void setDelDate(int delDate)
    {
        this.del_date = delDate;
    }

    public void setDelFlag(int delFlag)
    {
        this.del_flag = delFlag;
    }

    public void setPasswdUpdate(int passwdUpdate)
    {
        this.passwd_update = passwdUpdate;
    }

    public void setCustomId(String customId)
    {
        this.custom_id = customId;
    }

    /**
     * 社員データ取得
     * 
     * @param licenceKey ライセンスキー
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean getData(String staff_id)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;

        ret = false;

        query = "SELECT * FROM research_staff WHERE company_id = 2 AND staff_id = ?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setString( 1, staff_id );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    setData( result );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataResearchStaff.getData] Exception=" + e.toString() );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /**
     * 社員認証データ取得
     * 
     * @param result ユーザ基本データレコード
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean setData(ResultSet result)
    {
        boolean ret;
        ret = false;
        try
        {
            if ( result != null )
            {
                this.staff_id = result.getString( "staff_id" );
                this.name_last = result.getString( "name_last" );
                this.name_first = result.getString( "name_first" );
                this.name_kana_last = result.getString( "name_kana_last" );
                this.name_kana_first = result.getString( "name_kana_first" );
                this.section_id = result.getString( "section_id" );
                this.section_name = result.getString( "section_name" );
                this.mail1 = result.getString( "mail1" );
                this.mail2 = result.getString( "mail2" );
                this.phs = result.getString( "phs" );
                this.mail_mobile = result.getString( "mail_mobile" );
                this.machineid = result.getString( "machineid" );
                this.kind = result.getInt( "kind" );
                this.last_update = result.getInt( "last_update" );
                this.last_uptime = result.getInt( "last_uptime" );
                this.del_date = result.getInt( "del_date" );
                this.del_flag = result.getInt( "del_flag" );
                this.passwd_update = result.getInt( "passwd_update" );
                this.custom_id = result.getString( "custom_id" );
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataResearchStaff.setData] Exception=" + e.toString() );
        }

        return(ret);
    }

    /**
     * 会員履歴追加
     * 
     * @see "値のセット後(setXXX)に行うこと"
     * @return 処理結果(TRUE:正常,FALSE:異常)
     */
    public boolean insertHistory(String staff_id)
    {
        boolean ret = false;
        if ( getData( staff_id ) )
        {
            Connection connection = null;
            PreparedStatement prestate = null;
            String query;
            query = "INSERT hotenavi.research_staff_history SET ";
            query += " company_id = 2,";
            query += " staff_id = ?,";
            query += " name_last = ?,";
            query += " name_first = ?,";
            query += " name_kana_last = ?,";
            query += " name_kana_first = ?,";
            query += " section_id = ?,";
            query += " section_name = ?,";
            query += " mail1 = ?,";
            query += " mail2 = ?,";
            query += " phs = ?,";
            query += " mail_mobile = ?,";
            query += " machineid = ?,";
            query += " kind = ?,";
            query += " last_update = ?,";
            query += " last_uptime = ?,";
            query += " del_date = ?,";
            query += " del_flag = ?,";
            query += " passwd_update = ?,";
            query += " custom_id = ?";
            try
            {
                connection = DBConnection.getConnection();
                prestate = connection.prepareStatement( query );
                int i = 1;
                prestate.setString( i++, this.staff_id );
                prestate.setString( i++, this.name_last );
                prestate.setString( i++, this.name_first );
                prestate.setString( i++, this.name_kana_last );
                prestate.setString( i++, this.name_kana_first );
                prestate.setString( i++, this.section_id );
                prestate.setString( i++, this.section_name );
                prestate.setString( i++, this.mail1 );
                prestate.setString( i++, this.mail2 );
                prestate.setString( i++, this.phs );
                prestate.setString( i++, this.mail_mobile );
                prestate.setString( i++, this.machineid );
                prestate.setInt( i++, this.kind );
                prestate.setInt( i++, this.last_update );
                prestate.setInt( i++, this.last_uptime );
                prestate.setInt( i++, this.del_date );
                prestate.setInt( i++, this.del_flag );
                prestate.setInt( i++, this.passwd_update );
                prestate.setString( i++, this.custom_id );
                if ( prestate.executeUpdate() > 1 )
                {
                    ret = true;
                }
            }
            catch ( Exception e )
            {
                Logging.error( "[DataResearchStaff.insertHistory] Exception=" + e.toString() );
            }
            finally
            {
                DBConnection.releaseResources( prestate );
                DBConnection.releaseResources( connection );
            }
        }
        return(ret);
    }

}
