package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.happyhotel.common.DBConnection;
import jp.happyhotel.common.Logging;
import jp.happyhotel.common.Url;

/**
 * �L�����y�[�����}�X�^�iap_campaign_master�j�擾�N���X
 * 
 * @author Takeshi.Sakurai
 * @version 1.00 2014/8/13
 */
public class DataApCampaignMaster implements Serializable
{
    /**
     *
     */
    private static final long  serialVersionUID = 5722380022774706192L;
    public static final String TABLE            = "ap_campaign_master";
    private int                campaignId;                             // �L�����y�[��ID
    private String             title;                                  // �L�����y�[���^�C�g��
    private String             detail;                                 // �L�����y�[���ڍ�
    private String             contents;                               // �L�����y�[���p�R���e���c
    private String             url;                                    // URL
    private int                dispFrom;                               // �\���J�n��(YYYYMMDD)
    private int                dispFromTime;                           // �\���J�n����(HHMMSS)
    private int                dispTo;                                 // �\���I����(YYYYMMDD)
    private int                dispToTime;                             // �\���I������(HHMMSS)
    private int                limitFrom;                              // ���p�\�J�n��(YYYYMMDD)
    private int                limitFromTime;                          // ���p�\�J�n����(HHMMSS)
    private int                limitTo;                                // ���p�\�I����(YYYYMMDD)
    private int                limitToTime;                            // ���p�\�I������(HHMMSS)
    private int                useCount;                               // ���p�\��
    private int                id;                                     // �z�e��ID�i�z�e������̏ꍇ�j
    private int                prefId;                                 // �s���{���i�s���{������̏ꍇ�j
    private int                localId;                                // �n��R�[�h(�n�����̏ꍇ)

    /**
     * �f�[�^�����������܂��B
     */
    public DataApCampaignMaster()
    {
        this.campaignId = 0;
        this.title = "";
        this.detail = "";
        this.contents = "";
        this.url = "";
        this.dispFrom = 0;
        this.dispFromTime = 0;
        this.dispTo = 0;
        this.dispToTime = 0;
        this.limitFrom = 0;
        this.limitFromTime = 0;
        this.limitTo = 0;
        this.limitToTime = 0;
        this.useCount = 0;
        this.id = 0;
        this.prefId = 0;
        this.localId = 0;
    }

    public int getCampaignId()
    {
        return campaignId;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDetail()
    {
        return detail;
    }

    public String getContents()
    {
        return contents;
    }

    public String getUrl()
    {
        return Url.convertUrl( url );
    }

    public int getDispFrom()
    {
        return dispFrom;
    }

    public int getDispFromTime()
    {
        return dispFromTime;
    }

    public int getDispTo()
    {
        return dispTo;
    }

    public int getDispToTime()
    {
        return dispToTime;
    }

    public int getLimitFrom()
    {
        return limitFrom;
    }

    public int getLimitFromTime()
    {
        return limitFromTime;
    }

    public int getLimitTo()
    {
        return limitTo;
    }

    public int getLimitToTime()
    {
        return limitToTime;
    }

    public int getUseCount()
    {
        return useCount;
    }

    public int getId()
    {
        return id;
    }

    public int getPrefId()
    {
        return prefId;
    }

    public int getLocalId()
    {
        return localId;
    }

    public void setCampaignId(int campaignId)
    {
        this.campaignId = campaignId;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setDispFrom(int dispFrom)
    {
        this.dispFrom = dispFrom;
    }

    public void setDispFromTime(int dispFromTime)
    {
        this.dispFromTime = dispFromTime;
    }

    public void setDispTo(int dispTo)
    {
        this.dispTo = dispTo;
    }

    public void setDispToTime(int dispToTime)
    {
        this.dispToTime = dispToTime;
    }

    public void setLimitFrom(int limitFrom)
    {
        this.limitFrom = limitFrom;
    }

    public void setLimitFromTime(int limitFromTime)
    {
        this.limitFromTime = limitFromTime;
    }

    public void setLimitTo(int limitTo)
    {
        this.limitTo = limitTo;
    }

    public void setLimitToTime(int limitToTime)
    {
        this.limitToTime = limitToTime;
    }

    public void setUseCount(int useCount)
    {
        this.useCount = useCount;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setPrefId(int prefId)
    {
        this.prefId = prefId;
    }

    public void setLocalId(int localId)
    {
        this.localId = localId;
    }

    /****
     * �L�����y�[�����}�X�^�iap_campaign_master�j�擾
     * 
     * @param campaignId �L�����y�[��ID
     * @return
     */
    public boolean getData(int campaignId)
    {
        boolean ret;
        String query;
        Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_campaign_master WHERE campaign_id = ? ";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, campaignId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.campaignId = result.getInt( "campaign_id" );
                    this.title = result.getString( "title" );
                    this.detail = result.getString( "detail" );
                    this.contents = result.getString( "contents" );
                    this.url = result.getString( "url" );
                    this.dispFrom = result.getInt( "disp_from" );
                    this.dispFromTime = result.getInt( "disp_from_time" );
                    this.dispTo = result.getInt( "disp_to" );
                    this.dispToTime = result.getInt( "disp_to_time" );
                    this.limitFrom = result.getInt( "limit_from" );
                    this.limitFromTime = result.getInt( "limit_from_time" );
                    this.limitTo = result.getInt( "limit_to" );
                    this.limitToTime = result.getInt( "limit_to_time" );
                    this.useCount = result.getInt( "use_count" );
                    this.id = result.getInt( "id" );
                    this.prefId = result.getInt( "pref_id" );
                    this.localId = result.getInt( "local_id" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApCampaignMaster.getData] Exception=" + e.toString(), "DataApCampaignMaster" );
        }
        finally
        {
            DBConnection.releaseResources( result, prestate, connection );
        }
        return(ret);
    }

    /****
     * �L�����y�[�����}�X�^�iap_campaign_master�j�擾
     * 
     * @param connection �R�l�N�V����
     * @param campaignId �L�����y�[��ID
     * @return
     */
    public boolean getData(Connection connection, int campaignId)
    {
        boolean ret;
        String query;
        // /Connection connection = null;
        ResultSet result = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "SELECT * FROM ap_campaign_master WHERE campaign_id = ? ";
        try
        {
            prestate = connection.prepareStatement( query );
            prestate.setInt( 1, campaignId );
            result = prestate.executeQuery();
            if ( result != null )
            {
                if ( result.next() != false )
                {
                    this.campaignId = result.getInt( "campaign_id" );
                    this.title = result.getString( "title" );
                    this.detail = result.getString( "detail" );
                    this.contents = result.getString( "contents" );
                    this.url = result.getString( "url" );
                    this.dispFrom = result.getInt( "disp_from" );
                    this.dispFromTime = result.getInt( "disp_from_time" );
                    this.dispTo = result.getInt( "disp_to" );
                    this.dispToTime = result.getInt( "disp_to_time" );
                    this.limitFrom = result.getInt( "limit_from" );
                    this.limitFromTime = result.getInt( "limit_from_time" );
                    this.limitTo = result.getInt( "limit_to" );
                    this.limitToTime = result.getInt( "limit_to_time" );
                    this.useCount = result.getInt( "use_count" );
                    this.id = result.getInt( "id" );
                    this.prefId = result.getInt( "pref_id" );
                    this.localId = result.getInt( "local_id" );
                    ret = true;
                }
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApCampaignMaster.getData] Exception=" + e.toString(), "DataApCampaignMaster" );
        }
        finally
        {
            DBConnection.releaseResources( prestate );
        }
        return(ret);
    }

    /**
     * �L�����y�[�����}�X�^�iap_campaign_master�j�ݒ�
     * 
     * @param result �}�X�^�[�\�[�g���R�[�h
     * @return ��������(TRUE:����,FALSE:�ُ�)
     */
    public boolean setData(ResultSet result)
    {
        try
        {
            if ( result != null )
            {
                this.campaignId = result.getInt( "campaign_id" );
                this.title = result.getString( "title" );
                this.detail = result.getString( "detail" );
                this.contents = result.getString( "contents" );
                this.url = result.getString( "url" );
                this.dispFrom = result.getInt( "disp_from" );
                this.dispFromTime = result.getInt( "disp_from_time" );
                this.dispTo = result.getInt( "disp_to" );
                this.dispToTime = result.getInt( "disp_to_time" );
                this.limitFrom = result.getInt( "limit_from" );
                this.limitFromTime = result.getInt( "limit_from_time" );
                this.limitTo = result.getInt( "limit_to" );
                this.limitToTime = result.getInt( "limit_to_time" );
                this.useCount = result.getInt( "use_count" );
                this.id = result.getInt( "id" );
                this.prefId = result.getInt( "pref_id" );
                this.localId = result.getInt( "local_id" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApCampaignMaster.setData] Exception=" + e.toString() );
        }
        return(true);
    }

    /**
     * �L�����y�[�����}�X�^�iap_campaign_master�j�}��
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @return ��������(TRUE:����,FALSE:�ُ�)
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

        query = "INSERT ap_campaign_master SET ";
        query += " campaign_id=?";
        query += ", title=?";
        query += ", detail=?";
        query += ", contents=?";
        query += ", url=?";
        query += ", disp_from=?";
        query += ", disp_from_time=?";
        query += ", disp_to=?";
        query += ", disp_to_time=?";
        query += ", limit_from=?";
        query += ", limit_from_time=?";
        query += ", limit_to=?";
        query += ", limit_to_time=?";
        query += ", use_count=?";
        query += ", id=?";
        query += ", pref_id=?";
        query += ", local_id=?";
        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );

            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setInt( i++, this.campaignId );
            prestate.setString( i++, this.title );
            prestate.setString( i++, this.detail );
            prestate.setString( i++, this.contents );
            prestate.setString( i++, this.url );
            prestate.setInt( i++, this.dispFrom );
            prestate.setInt( i++, this.dispFromTime );
            prestate.setInt( i++, this.dispTo );
            prestate.setInt( i++, this.dispToTime );
            prestate.setInt( i++, this.limitFrom );
            prestate.setInt( i++, this.limitFromTime );
            prestate.setInt( i++, this.limitTo );
            prestate.setInt( i++, this.limitToTime );
            prestate.setInt( i++, this.useCount );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.prefId );
            prestate.setInt( i++, this.localId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApCampaignMaster.insertData] Exception=" + e.toString() );
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
     * �L�����y�[�����}�X�^�iap_campaign_master�j�X�V
     * 
     * @see "�l�̃Z�b�g��(setXXX)�ɍs������"
     * @param campaignId �L�����y�[��ID
     * @return
     */
    public boolean updateData(int campaignId)
    {
        int i = 1;
        int result;
        boolean ret;
        String query;
        Connection connection = null;
        PreparedStatement prestate = null;
        ret = false;
        query = "UPDATE ap_campaign_master SET ";
        query += " title=?";
        query += ", detail=?";
        query += ", contents=?";
        query += ", url=?";
        query += ", disp_from=?";
        query += ", disp_from_time=?";
        query += ", disp_to=?";
        query += ", disp_to_time=?";
        query += ", limit_from=?";
        query += ", limit_from_time=?";
        query += ", limit_to=?";
        query += ", limit_to_time=?";
        query += ", use_count=?";
        query += ", id=?";
        query += ", pref_id=?";
        query += ", local_id=?";
        query += " WHERE campaign_id=?";

        try
        {
            connection = DBConnection.getConnection();
            prestate = connection.prepareStatement( query );
            // �X�V�Ώۂ̒l���Z�b�g����
            prestate.setString( i++, this.title );
            prestate.setString( i++, this.detail );
            prestate.setString( i++, this.contents );
            prestate.setString( i++, this.url );
            prestate.setInt( i++, this.dispFrom );
            prestate.setInt( i++, this.dispFromTime );
            prestate.setInt( i++, this.dispTo );
            prestate.setInt( i++, this.dispToTime );
            prestate.setInt( i++, this.limitFrom );
            prestate.setInt( i++, this.limitFromTime );
            prestate.setInt( i++, this.limitTo );
            prestate.setInt( i++, this.limitToTime );
            prestate.setInt( i++, this.useCount );
            prestate.setInt( i++, this.id );
            prestate.setInt( i++, this.prefId );
            prestate.setInt( i++, this.localId );
            prestate.setInt( i++, this.campaignId );
            result = prestate.executeUpdate();
            if ( result > 0 )
            {
                ret = true;
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApCampaignMaster.updateData] Exception=" + e.toString() );
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
