package jp.happyhotel.common;

import jp.happyhotel.data.DataHotelElement;

/**
 * 新ホテナビ開始日付取得 *
 * 
 * @author T.Sakurai
 * @version 1.00 2017/05/09
 */
public class HotelElement
{

    /**
     * リニューアル開始日付取得
     * 
     * @param hotelId ホテルID
     * @return リニューアル開始日付
     */
    public static int getTrialDate(String hotelId)
    {
        DataHotelElement data = new DataHotelElement();
        int trialDate = 99999999;

        if ( data.getData( hotelId ) != false )
        {
            trialDate = data.getTrialDate();
        }
        return trialDate;
    }

    /**
     * 新ホテナビ稼動開始日付取得
     * 
     * @param hotelId ホテルID
     * @return 稼動開始日付
     */
    public static int getStartDate(String hotelId)
    {
        DataHotelElement data = new DataHotelElement();
        int startDate = 99999999;

        if ( data.getData( hotelId ) != false )
        {
            startDate = data.getStartDate();
        }
        return startDate;
    }
}
