package jp.happyhotel.others;

import java.util.Comparator;

import jp.happyhotel.data.DataSystemMyHotel;

/**
 * �}�C�z�e�������L���O�\�[�g�N���X
 * 
 * @see Comparator�̃C���^�t�F�[�X������
 * @author S.Tashiro
 * @version 1.00 2010/02/09
 */

public class SortMyHotelById implements Comparator<DataSystemMyHotel>
{
    /**
     * �z�e��ID�ł̔�r( DataSystemMyHotel.getCount() )
     * 
     * @param dsmh1 DataSystemMyHotel
     * @param dsmh2 DataSystemMyHotel
     * @return ��r����(0,-1,1)�����Ń\�[�g
     */
    public int compare(DataSystemMyHotel dsmh1, DataSystemMyHotel dsmh2)
    {
        int ret;
        ret = 0;
        if ( dsmh1.getId() == dsmh2.getId() )
        {
            ret = 0;
        }
        else if ( dsmh1.getId() > dsmh2.getId() )
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
