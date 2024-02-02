package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.data.DataApCampaignMaster;
import jp.happyhotel.data.DataApTokenUser;
import jp.happyhotel.data.DataApUserCampaign;
import jp.happyhotel.data.DataApUserPushConfig;

/**
 * Pushí ímèÓïÒ
 * 
 * @author S.Tashiro
 * @version 1.00 2011/05/19
 */
public class PushList implements Serializable
{
    DataApTokenUser[]      datu  = null;
    DataApUserPushConfig[] daupc = null;
    DataApCampaignMaster   dacm  = null;
    DataApUserCampaign[]   dauc  = null;

}
