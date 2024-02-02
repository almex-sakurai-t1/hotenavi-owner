package jp.happyhotel.data;

import java.io.Serializable;
import java.sql.ResultSet;

import jp.happyhotel.common.Logging;

/**
 * プッシュ配信データリスト拡張クラス
 *
 * @author Kaori.Mitsuhashi
 * @version 1.00 2016/6/8
 */
public class DataApPushDataListExtend extends DataApPushDataList implements Serializable
{
    /**
     *
     */
    private int                osType;                              // 1:iPhone,2:Android
    private int                pushFlag;                            // プッシュ通知全体設定(0:通知OK,1:通知拒否)
    private int                coFlag;                              // チェックアウト通知(0:通知OK,1:通知拒否)
    private int                campaignFlag;                        // ハピホテからのお知らせ(0:通知OK,1:通知拒否)



    /**
     * データを初期化します。
     */
    public DataApPushDataListExtend()
    {
        this.osType = 0;
        this.pushFlag = 0;
        this.coFlag = 0;
        this.campaignFlag = 0;
    }

    public int getOsType()
    {
        return osType;
    }

    public void setOsType(int osType)
    {
        this.osType = osType;
    }

    public void setPushFlag(int pushFlag)
    {
        this.pushFlag = pushFlag;
    }

    public int getPushFlag()
    {
        return pushFlag;
    }

    public void setCoFlag(int coFlag)
    {
        this.coFlag = coFlag;
    }

    public int getCoFlag()
    {
        return coFlag;
    }

    public void setCampaignFlag(int campaignFlag)
    {
        this.campaignFlag = campaignFlag;
    }

    public int getCampaignFlag()
    {
        return campaignFlag;
    }



    /**
     * キャンペーン用プッシュ配信データリスト設定
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
                super.setData( result );
                this.osType = result.getInt( "os_type" );
                this.pushFlag = result.getInt( "push_flag" );
                this.coFlag = result.getInt( "co_flag" );
                this.campaignFlag = result.getInt( "campaign_flag" );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[DataApPushDataListExtend.setData] Exception=" + e.toString() );
        }
        return(true);
    }
}
