package jp.happyhotel.common;

public class ConstantsMemberReg
{

    // 文字数
    public static final int  USERID_ABOVE        = 1;
    public static final int  USERID_FOLLOW       = 32;
    public static final int  PASSWORD_ABOVE      = 4;
    public static final int  PASSWORD_FOLLOW     = 8;
    public static final int  PASSWORD_ABOVE_SC   = 8;
    public static final int  PASSWORD_FOLLOW_SC  = 16;
    public static final int  NAME_ABOVE          = 1;
    public static final int  NAME_FOLLOW         = 40;
    public static final int  FURIGANA_ABOVE      = 1;
    public static final int  FURIGANA_FOLLOW     = 20;
    public static final int  HANDLENAME_ABOVE    = 1;
    public static final int  HANDLENAME_FOLLOW   = 40;
    // public static final int ADDRESS_ABOVE = 1;
    // public static final int ADDRESS_FOLLOW = 2;
    public static final int  TELLNUMBER_ABOVE    = 1;
    public static final int  TELLNUMBER_FOLLOW   = 15;
    public static final int  MAIL_ADDRESS_ABOVE  = 1;
    public static final int  MAIL_ADDRESS_FOLLOW = 63;

    // 入力項目
    public static final int  USER_ID             = 0; // ユーザーID
    public static final int  PASSWORD            = 1; // パスワード
    public static final int  PASSWORD_COFIRM     = 2; // パスワード(確認用)
    public static final int  NAME                = 3; // 名前
    public static final int  FURIGANA            = 4; // フリガナ
    public static final int  SEX                 = 5; // 性別
    public static final int  HANDLE_NAME         = 6; // ニックネーム
    public static final int  BIRTHDAY1_YEAR      = 7; // 誕生日1(年)
    public static final int  BIRTHDAY1_MONTH     = 8; // 誕生日1(月)
    public static final int  BIRTHDAY1_DAY       = 9; // 誕生日1(日)
    public static final int  BIRTHDAY2_YEAR      = 10; // 誕生日2(年)
    public static final int  BIRTHDAY2_MONTH     = 11; // 誕生日2(月)
    public static final int  BIRTHDAY2_DAY       = 12; // 誕生日2(日)
    public static final int  MEMORIALDAY1_YEAR   = 13; // 記念日1(年)
    public static final int  MEMORIALDAY1_MONTH  = 14; // 記念日1(月)
    public static final int  MEMORIALDAY1_DAY    = 15; // 記念日1(日)
    public static final int  MEMORIALDAY2_YEAR   = 16; // 記念日2(年)
    public static final int  MEMORIALDAY2_MONTH  = 17; // 記念日2(月)
    public static final int  MEMORIALDAY2_DAY    = 18; // 記念日2(日)
    public static final int  PREF_CODE1          = 19; // 都道府県1
    public static final int  JISCODE1            = 20; // 市区町村1
    public static final int  PREF_CODE2          = 21; // 都道府県2
    public static final int  JISCODE2            = 22; // 市区町村2
    public static final int  TELL_NUMBER1        = 23; // 電話番号1
    public static final int  TELL_NUMBER2        = 24; // 電話番号2
    public static final int  MAIL_ADDRESS        = 25; // メールアドレス
    public static final int  MAIL_ADDRESS_COFIRM = 26; // メールアドレス(確認用)
    public static final int  MAIL_MAGAZINE       = 27; // メルマガ

    // error項目
    public static final int  UNINPUTTED_ERR      = 0; // 未入力
    public static final int  SPACE_ERR           = 1; // 空白のみ
    public static final int  STRING_TYPE__ERR    = 2; // 全半角チェック
    public static final int  NUM_OF_CHARA_ERR    = 3; // 入力文字数制限
    public static final int  ALREADY_USED_ERR    = 4; // すでに登録済
    public static final int  REGIST_ERR          = 5; // 登録不可
    public static final int  COMPARE_ERR         = 6; // (確認用)との相違
    public static final int  MAIL_FORMAT_ERR     = 7; // メールアドレスフォーマット
    public static final int  MAIL_INTERIM_REG_   = 8; // 仮登録完了
    public static final int  MAIL_REG_COMPLETED  = 9; // 登録完了
    public static final int  NUM_OF_CHARA_ERR_SC = 10; // 入力文字数制限(ステイコンシェルジュ)

    public static String[][] errorMessageTable;

    static
    {
        errorMessageTable = new String[30][11];
        errorMessageTable[USER_ID][UNINPUTTED_ERR] = "「ユーザーID」が入力されていません。";
        errorMessageTable[USER_ID][STRING_TYPE__ERR] = "「ユーザーID」は半角英数で入力してください。（数字のみのIDは登録できません。）";
        errorMessageTable[USER_ID][NUM_OF_CHARA_ERR] = "「ユーザーID」は4～20文字で入力してください。";
        errorMessageTable[USER_ID][ALREADY_USED_ERR] = "入力した「ユーザーID」はすでに使用されています。";
        errorMessageTable[USER_ID][REGIST_ERR] = "入力した「ユーザーID」は登録できません。";

        errorMessageTable[PASSWORD][UNINPUTTED_ERR] = "「パスワード」が入力されていません。";
        errorMessageTable[PASSWORD][STRING_TYPE__ERR] = "「パスワード」は、半角英数字で入力してください。";
        errorMessageTable[PASSWORD][NUM_OF_CHARA_ERR] = "「パスワード」は、4～8文字で入力してください。";
        errorMessageTable[PASSWORD][REGIST_ERR] = "「ユーザーID」と「パスワード」は違うものを入力してください。";
        errorMessageTable[PASSWORD][COMPARE_ERR] = "「パスワード」と「パスワード（確認用）」が違っています。";
        errorMessageTable[PASSWORD][NUM_OF_CHARA_ERR_SC] = "「パスワード」は、8～16文字で入力してください。";

        errorMessageTable[NAME][UNINPUTTED_ERR] = "「名前」が入力されていません。";
        errorMessageTable[NAME][SPACE_ERR] = "空白のみの「名前」は登録できません。";
        errorMessageTable[NAME][STRING_TYPE__ERR] = "「名前」は全角で入力してください。";
        errorMessageTable[NAME][NUM_OF_CHARA_ERR] = "「名前」は1～20文字で入力してください。";

        errorMessageTable[FURIGANA][UNINPUTTED_ERR] = "「フリガナ」が入力されていません。";
        errorMessageTable[FURIGANA][STRING_TYPE__ERR] = "「フリガナ」は全半角カナで入力してください。";
        errorMessageTable[FURIGANA][NUM_OF_CHARA_ERR] = "「フリガナ」は1～20文字で入力してください。";

        errorMessageTable[SEX][UNINPUTTED_ERR] = "「性別」が選択されていません。";

        errorMessageTable[HANDLE_NAME][UNINPUTTED_ERR] = "「ニックネーム」が入力されていません。";
        errorMessageTable[HANDLE_NAME][SPACE_ERR] = "空白のみの「ニックネーム」は登録できません。";
        errorMessageTable[HANDLE_NAME][STRING_TYPE__ERR] = "「ニックネーム」は全角で入力してください。";
        errorMessageTable[HANDLE_NAME][NUM_OF_CHARA_ERR] = "「ニックネーム」は1～20文字で入力してください。";
        errorMessageTable[HANDLE_NAME][ALREADY_USED_ERR] = "入力した「ニックネーム」はすでに使用されています。";
        errorMessageTable[HANDLE_NAME][REGIST_ERR] = "入力した「ニックネーム」は登録できません。";

        errorMessageTable[BIRTHDAY1_YEAR][UNINPUTTED_ERR] = "「生年月日1（年）」が選択されていません。";
        errorMessageTable[BIRTHDAY1_MONTH][UNINPUTTED_ERR] = "「生年月日1（月）」が選択されていません。";
        errorMessageTable[BIRTHDAY1_DAY][UNINPUTTED_ERR] = "「生年月日1（日）」が選択されていません。";
        errorMessageTable[BIRTHDAY1_DAY][REGIST_ERR] = "正しい「生年月日1（日）」を選択してください。";

        errorMessageTable[BIRTHDAY2_YEAR][UNINPUTTED_ERR] = "「生年月日2（年）」が選択されていません。";
        errorMessageTable[BIRTHDAY2_MONTH][UNINPUTTED_ERR] = "「生年月日2（月）」が選択されていません。";
        errorMessageTable[BIRTHDAY2_DAY][UNINPUTTED_ERR] = "「生年月日2（日）」が選択されていません。";
        errorMessageTable[BIRTHDAY2_DAY][REGIST_ERR] = "正しい「生年月日2（日）」を選択してください。";

        errorMessageTable[MEMORIALDAY1_YEAR][UNINPUTTED_ERR] = "「記念日1（年）」が選択されていません。";
        errorMessageTable[MEMORIALDAY1_MONTH][UNINPUTTED_ERR] = "「記念日1（月）」が選択されていません。";
        errorMessageTable[MEMORIALDAY1_DAY][UNINPUTTED_ERR] = "「記念日1（日）」が選択されていません。";
        errorMessageTable[MEMORIALDAY1_DAY][REGIST_ERR] = "正しい「記念日1（日）」を選択してください。";

        errorMessageTable[MEMORIALDAY2_YEAR][UNINPUTTED_ERR] = "「記念日2（年）」が選択されていません。";
        errorMessageTable[MEMORIALDAY2_MONTH][UNINPUTTED_ERR] = "「記念日2（月）」が選択されていません。";
        errorMessageTable[MEMORIALDAY2_DAY][UNINPUTTED_ERR] = "「記念日2（日）」が選択されていません。";
        errorMessageTable[MEMORIALDAY2_DAY][REGIST_ERR] = "正しい「記念日2（日）」を選択してください。";

        errorMessageTable[PREF_CODE1][UNINPUTTED_ERR] = "「お住まいの都道府県1」が選択されていません。";
        errorMessageTable[JISCODE1][UNINPUTTED_ERR] = "「お住まいの市区町村1」が選択されていません。";
        errorMessageTable[PREF_CODE2][UNINPUTTED_ERR] = "「お住まいの都道府県2」が選択されていません。";
        errorMessageTable[JISCODE2][UNINPUTTED_ERR] = "「お住まいの市区町村2」が選択されていません。";

        errorMessageTable[TELL_NUMBER1][UNINPUTTED_ERR] = "「電話番号1」が入力されていません。";
        errorMessageTable[TELL_NUMBER1][STRING_TYPE__ERR] = "「電話番号1」は半角数字で入力してください。";
        errorMessageTable[TELL_NUMBER1][NUM_OF_CHARA_ERR] = "「電話番号1」は15文字で入力してください。";
        errorMessageTable[TELL_NUMBER2][UNINPUTTED_ERR] = "「電話番号2」が入力されていません。";
        errorMessageTable[TELL_NUMBER2][STRING_TYPE__ERR] = "「電話番号2」は半角数字で入力してください。";
        errorMessageTable[TELL_NUMBER2][NUM_OF_CHARA_ERR] = "「電話番号2」は15文字で入力してください。";

        errorMessageTable[MAIL_ADDRESS][UNINPUTTED_ERR] = "「メールアドレス」が入力されていません。";
        errorMessageTable[MAIL_ADDRESS][MAIL_FORMAT_ERR] = "「メールアドレス」を正しく入力してください。";
        errorMessageTable[MAIL_ADDRESS][REGIST_ERR] = "入力した「メールアドレス」は登録できません。";
        errorMessageTable[MAIL_ADDRESS][COMPARE_ERR] = "「メールアドレス」と「メールアドレス(確認用)」が違っています。";
        // errorMessageTable[MAIL_ADDRESS_COFIRM][UNINPUTTED_ERR] = "「メールアドレス(確認用)」が入力されていません。";
        errorMessageTable[MAIL_ADDRESS][MAIL_INTERIM_REG_] = "入力した「メールアドレス」で仮登録が完了しています。ご登録のメールアドレス宛にご確認のメールをお送りしておりますので、URLをクリックし会員登録を完了してください。ご確認のメールが届いていない場合は、ご利用のメールアドレスを再度ご確認ください。";
        errorMessageTable[MAIL_ADDRESS][MAIL_REG_COMPLETED] = "入力した「メールアドレス」で登録が完了しています。";

        errorMessageTable[MAIL_MAGAZINE][UNINPUTTED_ERR] = "「メルマガ登録」が選択されていません。";
    }
}
