package jp.happyhotel.others;

import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelCi;
import jp.happyhotel.owner.BkoAccountRecv;
import jp.happyhotel.owner.LogicOwnerBkoHapiTouch;
import jp.happyhotel.user.UserPointPayTemp;

/**
 * �n�s�^�b�`�o�b�N�I�t�B�X�����N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2012/06/12
 */
public class HapiTouchBko
{

    /****
     * �o�b�N�I�t�B�X�ǉ�����
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @param pointKind �|�C���g�敪
     * @param dhc �z�e���`�F�b�N�C���f�[�^�iDataHotelCi�j
     * @return
     */
    public boolean addBkoData(String userId, int hotelId, int pointKind, DataHotelCi dhc)
    {
        boolean ret = false;
        boolean retUppt = false;
        boolean retBko = false;
        UserPointPayTemp uppt;
        uppt = new UserPointPayTemp();

        try
        {
            retUppt = uppt.getUserPointHistory( userId, hotelId, pointKind, dhc.getUserSeq(), dhc.getVisitSeq() );
            if ( retUppt != false )
            {
                BkoAccountRecv bko;
                bko = new BkoAccountRecv();
                retBko = bko.getDetailData( uppt.getUserPoint()[0] );
                if ( retBko ) // �ڍ׃f�[�^���������̂ŁA������
                {

                }
                else
                {
                    // �o�b�N�I�t�B�X�ɓo�^
                    LogicOwnerBkoHapiTouch bkoHapiTouch;
                    bkoHapiTouch = new LogicOwnerBkoHapiTouch();

                    ret = bkoHapiTouch.execInsert( userId, uppt.getUserPoint()[0], dhc.getSeq() );
                }
            }
        }
        catch ( Exception e )
        {
            ret = false;
            Logging.error( "[HapiTouchBko.addBkoData] Exception:" + e.toString(), "addBkoData" );
        }
        return ret;
    }

    /****
     * �o�b�N�I�t�B�X�X�V����
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @param pointKind �|�C���g�敪
     * @param dhc �z�e���`�F�b�N�C���f�[�^�iDataHotelCi�j
     * @return
     */
    public boolean updateBkoData(String userId, int hotelId, int pointKind, DataHotelCi dhc)
    {
        boolean retData = false;
        UserPointPayTemp uppt;
        uppt = new UserPointPayTemp();

        try
        {
            retData = uppt.getUserPointHistory( userId, hotelId, pointKind, dhc.getUserSeq(), dhc.getVisitSeq() );
            if ( retData != false )
            {
                // �o�b�N�I�t�B�X�ɓo�^
                LogicOwnerBkoHapiTouch bkoHapiTouch;
                bkoHapiTouch = new LogicOwnerBkoHapiTouch();

                retData = bkoHapiTouch.execUpdate( userId, uppt.getUserPoint()[0] );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchBko.upDataBkoData] Exception:" + e.toString() );
        }
        return retData;
    }

    /****
     * �o�b�N�I�t�B�X�X�V����
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @param pointKind �|�C���g�敪
     * @param dhc �z�e���`�F�b�N�C���f�[�^�iDataHotelCi�j
     * @return
     */
    public boolean updateBkoData(String userId, int hotelId, DataHotelCi dhc)
    {
        boolean retData = false;
        boolean retTemp = false;

        UserPointPayTemp uppt;
        uppt = new UserPointPayTemp();

        try
        {
            retTemp = uppt.getUserPointHistory( userId, hotelId, dhc.getUserSeq(), dhc.getVisitSeq() );
            if ( retTemp != false )
            {
                // �o�b�N�I�t�B�X�ɓo�^
                LogicOwnerBkoHapiTouch bkoHapiTouch;
                bkoHapiTouch = new LogicOwnerBkoHapiTouch();

                retData = bkoHapiTouch.execUpdate( userId, dhc );
            }
            else
            {
                retData = true; // �o�b�N�I�t�B�X�ɓo�^������̂��Ȃ��̂ŃG���[�ɂ͂��Ȃ��B
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchBko.upDataBkoData] Exception:" + e.toString() );
        }
        return retData;
    }

    /****
     * �o�b�N�I�t�B�X�L�����Z������
     * 
     * @param userId ���[�UID
     * @param hotelId �z�e��ID
     * @param pointKind �|�C���g�敪
     * @param dhc �z�e���`�F�b�N�C���f�[�^�iDataHotelCi�j
     * @return
     */
    public boolean cancelBkoData(String userId, int hotelId, int pointKind, DataHotelCi dhc)
    {
        boolean retData = false;
        UserPointPayTemp uppt;
        uppt = new UserPointPayTemp();

        try
        {
            retData = uppt.getUserPointHistory( userId, hotelId, pointKind, dhc.getUserSeq(), dhc.getVisitSeq() );
            if ( retData != false )
            {
                // �o�b�N�I�t�B�X�ɓo�^
                LogicOwnerBkoHapiTouch bkoHapiTouch;
                bkoHapiTouch = new LogicOwnerBkoHapiTouch();

                retData = bkoHapiTouch.execCancel( userId, uppt.getUserPoint()[0] );

            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchBko.cancelBkoData] Exception:" + e.toString() );
        }
        return retData;
    }
}
