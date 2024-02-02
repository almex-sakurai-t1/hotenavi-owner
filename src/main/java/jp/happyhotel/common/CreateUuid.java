/*
 * @(#)RandomString.java 1.00
 * 2009/07/15 Copyright (C) ALMEX Inc. 2009
 * ランダム文字列取得クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;
import java.util.UUID;

import jp.happyhotel.data.DataApUuid;

/**
 * UUID作成クラス
 * 
 * @author S.Tashiro
 * @version 1.00 2014/12/14
 */
public class CreateUuid implements Serializable
{

    /**
     * UUIDを作成
     * 
     * @param digit ランダム文字列の桁数
     * @return 処理結果("":失敗)
     */
    static public String create()
    {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    /**
     * 重複していないUUIDを返す。
     * 
     * @return 処理結果("":失敗)
     */
    public static String getUuid()
    {
        String uuid = "";
        DataApUuid dau = new DataApUuid();

        dau = new DataApUuid();

        // 問題のないUUIDが生成されたらbreak
        while( true )
        {
            uuid = create();
            // 同じUUIDが登録されていなければOK
            if ( dau.getData( uuid ) == false )
            {
                break;
            }
        }
        return(uuid);
    }
}
