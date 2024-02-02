package jp.happyhotel.common;

/**
 * 設定情報クラス
 * 
 */
public class Constants
{
    public static final int     pageLimitRecords         = 20;
    public static final int     pageLimitRecordMobile    = 10;
    public static final int     maxRecords               = 200;
    public static final int     maxRecordsHotenavi       = 500;
    public static final int     maxRecordsMobile         = 50;

    public static String        configFilesPath          = "//etc//happyhotel//";
    public static final String  errorRecordsNotFound1    = "指定したフリーワードのホテルは見つかりませんでした。";
    public static final String  errorRecordsNotFound2    = "見つかりませんでした。";
    public static final String  errorRecordsNotFound3    = "検索結果が200件を超えました。絞込みを行ってください。";
    public static final String  errorRecordsNotFound4    = "検索結果が500件を超えました。絞込みを行ってください。";
    public static final String  errorRecordsMyHotel      = "登録済みホテルがありません。";
    public static final String  errorRecordsHotelMembers = "カードレスメンバー登録済み\nホテルがありません。";
    public static final boolean zeroResultDisplay        = true;
    public static final boolean shiborikomiByPref        = false;

    public static final String  errorLimitFreeword       = "検索結果が" + maxRecords + "件を超えました。フリーワードの絞込みを行ってください。";
    public static final String  errorLimitRecords        = "検索結果が200件を超えました。絞込みを行ってください。";
    public static final String  ROOT_TAG_NAME_HAPPYHOTEL = "happyhotel";
    public static String        HAPPYHOTEL_URL           = "http://happyhotel.jp";

    public static final int     ERROR_CODE_API1          = 1;
    public static final int     ERROR_CODE_API2          = 2;
    public static final int     ERROR_CODE_API3          = 3;
    public static final int     ERROR_CODE_API4          = 4;
    public static final int     ERROR_CODE_API5          = 5;
    public static final int     ERROR_CODE_API6          = 6;
    public static final int     ERROR_CODE_API7          = 7;
    public static final int     ERROR_CODE_API8          = 8;
    public static final int     ERROR_CODE_API9          = 9;
    public static final int     ERROR_CODE_API10         = 10;
    public static final int     ERROR_CODE_API11         = 11;
    public static final int     ERROR_CODE_API12         = 12;
    public static final int     ERROR_CODE_API13         = 13;
    public static final int     ERROR_CODE_API14         = 14;
    public static final int     ERROR_CODE_API15         = 15;
    public static final int     ERROR_CODE_API16         = 16;
    public static final int     ERROR_CODE_API17         = 17;
    public static final int     ERROR_CODE_API18         = 18;
    public static final int     ERROR_CODE_API19         = 19;
    public static final int     ERROR_CODE_API20         = 20;
    public static final int     ERROR_CODE_API21         = 21;

    public static final String  ERROR_MSG_API1           = "ユーザIDが見つかりませんでした。";
    public static final String  ERROR_MSG_API2           = "パスワードが一致しません。";
    public static final String  ERROR_MSG_API3           = "会員情報が取得できません。\n設定メニューからID・パスワードが正しいかご確認ください。";
    public static final String  ERROR_MSG_API4           = "パラメータが正しくありません。";
    public static final String  ERROR_MSG_API5           = "パラメータが不足しています。";
    public static final String  ERROR_MSG_API6           = "該当ホテルは0件です。";
    public static final String  ERROR_MSG_API7           = "まだクチコミがありません。";
    public static final String  ERROR_MSG_API8           = "クチコミできないホテルです。";
    public static final String  ERROR_MSG_API9           = "例外が発生しました。";
    public static final String  ERROR_MSG_API10          = "ただいまアクセスが集中しております。しばらく時間をおいてから再度お試しください。";
    public static final String  ERROR_MSG_API11          = "見つかりませんでした";
    public static final String  ERROR_MSG_API12          = "ユーザIDまたはパスワードが一致しません";
    public static final String  ERROR_MSG_API13          = "既に会員登録済みです";
    public static final String  ERROR_MSG_API14          = "仮会員登録に失敗しました";
    public static final String  ERROR_MSG_API15          = "マイホテル削除に失敗しました";
    public static final String  ERROR_MSG_API16          = "ハピホテ会員情報の設定が必要です";
    public static final String  ERROR_MSG_API17          = "デバイストークンの登録に失敗しました";
    public static final String  ERROR_MSG_API18          = "UUIDの登録に失敗しました";
    public static final String  ERROR_MSG_API19          = "UUID情報の取得に失敗しました";
    public static final String  ERROR_MSG_API20          = "レシートの検証に失敗しました";
    public static final String  ERROR_MSG_API21          = "レシートの有効期限が切れています";
    public static final String  ERROR_MSG_API22          = "再度ログインしてください";

    public static final int     portNoHotenavi           = 7023;
    public static final int     portNoHappyhotel         = 7046;
    public static final int     timeoutForHost           = 5000;

    /** 最新フラグ - 最新 */
    public static final int     LATEST_FLAG_LATEST       = 1;
    /** 掲載フラグ - 掲載 */
    public static final int     PUBLISHING_FLAG_PUBLISH  = 1;

    /**
     * ログイン履歴ー区分
     * ハッピホテル：0
     */
    public static final int     LOGIN_KIND_HAPPYHOTEL    = 0;
    /**
     * ログイン履歴ー区分
     * ソーシャルログイン：1
     */
    public static final int     LOGIN_KIND_SOCIALLOGIN   = 1;
    /**
     * ログイン履歴ー端末
     * 統合アプリ：0
     */
    public static final int     LOGIN_TERMINAL_APP       = 0;
    /**
     * ログイン履歴ー端末
     * スマホＷＥＢ：1
     */
    public static final int     LOGIN_TERMINAL_SP_WEB    = 1;
    /**
     * ログイン履歴ー端末
     * PCＷＥＢ：2
     */
    public static final int     LOGIN_TERMINAL_PC_WEB    = 2;
    /**
     * ログイン履歴ー端末
     * 予約アプリ：3
     */
    public static final int     LOGIN_TERMINAL_RSV_APP   = 3;
    /**
     * ログイン履歴ー端末
     * 検索アプリ：4
     */
    public static final int     LOGIN_TERMINAL_SEL_APP   = 4;

    static
    {
        if ( System.getProperty( "os.name" ).toLowerCase().indexOf( "window" ) != -1 )
        {
            configFilesPath = "C:\\ALMEX\\WWW\\WEB-INF\\";
        }
        else
        {
            configFilesPath = "//etc//happyhotel//";
        }
    }
    public static final int     MINIMUM_PASSWORD_LENGTH  = 8;
    public static final int     MAXIMUM_PASSWORD_LENGTH  = 32;
}
