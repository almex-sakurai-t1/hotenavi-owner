package jp.happyhotel.others;

import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelCi;
import jp.happyhotel.owner.BkoAccountRecv;
import jp.happyhotel.owner.LogicOwnerBkoHapiTouch;
import jp.happyhotel.user.UserPointPayTemp;

/**
 * ハピタッチバックオフィス処理クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2012/06/12
 */
public class HapiTouchBko
{

    /****
     * バックオフィス追加処理
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @param pointKind ポイント区分
     * @param dhc ホテルチェックインデータ（DataHotelCi）
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
                if ( retBko ) // 詳細データがあったので、書換え
                {

                }
                else
                {
                    // バックオフィスに登録
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
     * バックオフィス更新処理
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @param pointKind ポイント区分
     * @param dhc ホテルチェックインデータ（DataHotelCi）
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
                // バックオフィスに登録
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
     * バックオフィス更新処理
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @param pointKind ポイント区分
     * @param dhc ホテルチェックインデータ（DataHotelCi）
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
                // バックオフィスに登録
                LogicOwnerBkoHapiTouch bkoHapiTouch;
                bkoHapiTouch = new LogicOwnerBkoHapiTouch();

                retData = bkoHapiTouch.execUpdate( userId, dhc );
            }
            else
            {
                retData = true; // バックオフィスに登録するものがないのでエラーにはしない。
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[HapiTouchBko.upDataBkoData] Exception:" + e.toString() );
        }
        return retData;
    }

    /****
     * バックオフィスキャンセル処理
     * 
     * @param userId ユーザID
     * @param hotelId ホテルID
     * @param pointKind ポイント区分
     * @param dhc ホテルチェックインデータ（DataHotelCi）
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
                // バックオフィスに登録
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
