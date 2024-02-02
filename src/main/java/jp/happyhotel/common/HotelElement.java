package jp.happyhotel.common;

import jp.happyhotel.data.DataHotelElement;

/**
 * �V�z�e�i�r�J�n���t�擾 *
 * 
 * @author T.Sakurai
 * @version 1.00 2017/05/09
 */
public class HotelElement
{

    /**
     * ���j���[�A���J�n���t�擾
     * 
     * @param hotelId �z�e��ID
     * @return ���j���[�A���J�n���t
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
     * �V�z�e�i�r�ғ��J�n���t�擾
     * 
     * @param hotelId �z�e��ID
     * @return �ғ��J�n���t
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
