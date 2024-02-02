package jp.happyhotel.others;

import java.util.Comparator;

import jp.happyhotel.data.DataSystemMyHotel;

/**
 * マイホテルランキングソートクラス
 * 
 * @see Comparatorのインタフェースを実装
 * @author S.Tashiro
 * @version 1.00 2010/2/09
 */

public class SortMyHotelByRegistration implements Comparator<DataSystemMyHotel>
{
    /**
     * マイホテル登録数での比較( DataSystemMyHotel.getCount() )
     * 
     * @param dsmh1 DataSystemMyHotel
     * @param dsmh2 DataSystemMyHotel
     * @return 比較結果(0,-1,1)降順でソート
     */
    public int compare(DataSystemMyHotel dsmh1, DataSystemMyHotel dsmh2)
    {
        int ret;
        ret = 0;
        if ( dsmh1.getCount() == dsmh2.getCount() )
        {
            ret = 0;
        }
        else if ( dsmh1.getCount() < dsmh2.getCount() )
        {
            ret = 1;
        }
        else
        {
            ret = -1;
        }

        return(ret);
    }
}
