/*
 * @(#)UserBasicInfo.java 1.00 2007/07/31 Copyright (C) ALMEX Inc. 2007 ユーザ基本情報取得クラス
 */

package jp.happyhotel.user;

import java.io.Serializable;

import jp.happyhotel.common.DateEdit;
import jp.happyhotel.data.DataUserLock;

/**
 * ユーザロッククラス。 ユーザのロック情報を取得する機能を提供する
 * 
 * @author S.Tashiro
 * @version 1.00 2013/02/15
 */
public class UserLock implements Serializable
{
    DataUserLock lock;

    public UserLock()
    {
    }

    public DataUserLock getUserInfo()
    {
        return lock;
    }

    /**
     * ユーザ情報取得
     * 
     * @param userId
     * @return
     */
    public boolean getUserInfo(String userId)
    {
        boolean ret = false;
        DataUserLock dul = new DataUserLock();

        ret = dul.getData( userId );
        this.lock = dul;
        return ret;
    }

    /**
     * 間違い回数追加
     * 
     * @param userId
     * @return
     */
    public boolean addMistakeCount(String userId)
    {
        boolean ret = false;
        DataUserLock dul = new DataUserLock();

        ret = dul.getData( userId );
        if ( ret != false )
        {
            ret = isValidMistakeTime( dul );
            if ( ret != false )
            {
                dul.setMistakeCount( dul.getMistakeCount() + 1 );
                if ( dul.getMistakeCount() == DataUserLock.LOCK_COUNT )
                {
                    // ロックを行うため、ステータス、日付時刻を更新
                    dul.setLockStatus( DataUserLock.LOCK );
                    dul.setLockDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                    dul.setLockTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                }
                ret = dul.updateData( userId );
            }
            else
            {
                dul.setMistakeCount( 1 );
                // 回数リセット時に、間違い日付、時刻を更新
                dul.setMistakeDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
                dul.setMistakeTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
                ret = dul.updateData( userId );
            }
        }
        else
        {
            dul.setUserId( userId );
            dul.setMistakeCount( 1 );
            dul.setMistakeDate( Integer.parseInt( DateEdit.getDate( 2 ) ) );
            dul.setMistakeTime( Integer.parseInt( DateEdit.getTime( 1 ) ) );
            ret = dul.insertData();
        }

        return false;
    }

    /**
     * 間違い時の時間内かどうか
     * 
     * @param dul
     * @return
     */
    public boolean isValidMistakeTime(DataUserLock dul)
    {
        boolean ret = false;
        if ( dul.getUserId().equals( "" ) == false )
        {
            ret = DateEdit.isValidDate( dul.getMistakeDate(), dul.getMistakeTime(), DataUserLock.KIND_MINUTES, DataUserLock.AVAILABLE_MINUTE );
        }
        return ret;
    }

    /**
     * ロック時間が有効時間内かどうか
     * 
     * @param dul
     * @return
     */
    public boolean isValidLockTime(DataUserLock dul)
    {
        boolean ret = false;
        if ( dul.getUserId().equals( "" ) == false )
        {
            ret = DateEdit.isValidDate( dul.getLockDate(), dul.getLockTime(), DataUserLock.KIND_MINUTES, DataUserLock.AVAILABLE_MINUTE );
        }

        return ret;
    }

    /**
     * ロック解除
     * 
     * @param userId
     * @return true:ロック解除成功（未ロック）,false:ロック解除失敗
     */
    public boolean releaseRock(String userId)
    {
        boolean ret = false;
        boolean retLock = false;
        DataUserLock dul = new DataUserLock();

        ret = dul.getData( userId );
        if ( ret != false )
        {
            if ( dul.getLockStatus() == DataUserLock.LOCK )
            {
                // ロックの有効時間をチェックする
                retLock = isValidLockTime( dul );
                if ( retLock == false )
                {
                    dul.setLockStatus( DataUserLock.RELEASE );
                    ret = dul.updateData( userId );
                }
                else
                {
                    // 有効時間内（ロック中）
                    ret = false;
                }
            }
            else
            {
                ret = true;
            }
        }
        else
        {
            ret = true;
        }

        return ret;

    }
}
