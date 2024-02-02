package jp.happyhotel.touch;

import java.io.Serializable;

import jp.happyhotel.common.Logging;
import jp.happyhotel.util.AccessToken;

import org.apache.commons.lang.StringUtils;

/**
 * タッチ用ユーザ認証
 * 
 * アプリからのアクセスTokenに基づきユーザ認証を行う
 */
public class UserAuth implements Serializable
{
    public static String getUserId(String accessToken)
    {
        String userId = "";
        accessToken = StringUtils.defaultIfEmpty( accessToken, "" );

        // トークンを受け取った場合、トークンに設定されたユーザIDとそのユーザのパスワードを変数に設定
        if ( accessToken.equals( "" ) == false )
        {
            // トークンの検証に失敗
            if ( AccessToken.verify( accessToken ) == false )
            {
                Logging.warn( "token verification failed. (token: " + accessToken + ")" );
            }

            // トークンの有効期限切れ
            if ( AccessToken.isWithinExpirationTime( accessToken ) == false )
            {
                Logging.warn( "token has expired. (token: " + accessToken + ")" );
            }

            userId = AccessToken.getUserId( accessToken );
        }
        return userId;
    }

}
