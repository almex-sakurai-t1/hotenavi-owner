package jp.happyhotel.common;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * クッキーを使った簡易セッション管理クラス
 *
 */
public class SessionManager
{
    /** 簡易セッション変数格納庫 */
    private static LinkedHashMap<String, Object> SES_MAP = new LinkedHashMap<String, Object>();
    /** 簡易セッション最大保持個数 */
    private static final int MAX_COUNT = 100;

    /**
     * 簡易セッション追加処理
     * @param sessionId セッションID
     * @param obj オブジェクト
     */
    public static void addSession(String sessionId, Object obj)
    {
        try
        {
            boolean isExistKey = SES_MAP.containsKey(sessionId);

            if(isExistKey == false && SES_MAP.size() >= MAX_COUNT)
            {
                // 新規に追加する分1つ最古のセッションを削除する
                for (Entry<String, Object> entry : SES_MAP.entrySet()) {
                    SES_MAP.remove(entry);
                    break;
                }
            }
            SES_MAP.put(sessionId, obj);
        }
        catch(Exception ex)
        {
            Logging.error( "[SessionManager addSession]Exception:" + ex.toString() );
        }
        finally
        {

        }

        return;
    }
    /**
     * 簡易セッション取得処理
     * @param sessionId セッションID
     * @return obj オブジェクト
     */
    public static Object getAttribute(String key)
    {
        Object ret = null;

        try
        {
            if(SES_MAP.containsKey(key) == true)
            {
                ret = SES_MAP.get(key);
            }
        }
        catch(Exception ex)
        {
            Logging.error( "[SessionManager getAttribute]Exception:" + ex.toString() );
        }
        finally
        {

        }

        return(ret);
    }


}
