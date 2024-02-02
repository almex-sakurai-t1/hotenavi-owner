package jp.happyhotel.common;

import static jp.happyhotel.common.CheckString.*;
import static jp.happyhotel.common.ConstantsMemberReg.*;
import static jp.happyhotel.common.ReplaceString.*;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public class MemberRegistValidationCheck
{

    /**
     * ユーザIDチェック
     * 
     * @param userId
     * @return errorMessage
     */
    public static String userIdValidationCheck(String userId, boolean allowNull)
    {
        String errorMessage = "";
        if ( !isValidParameter( userId ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[USER_ID][UNINPUTTED_ERR];
            }
        }
        else if ( !numAlphaCheck( userId ) )
        { // 半角英数以外は受け付けない
            errorMessage = errorMessageTable[USER_ID][STRING_TYPE__ERR];
        }
        else if ( !LengthCheck( userId, USERID_ABOVE, USERID_FOLLOW ) )
        { // 文字数が指定範囲以外の場合
            errorMessage = errorMessageTable[USER_ID][NUM_OF_CHARA_ERR];
        }
        else if ( numCheck( userId ) )
        { // 数字のみのIDは受け付けない
            errorMessage = errorMessageTable[USER_ID][STRING_TYPE__ERR];
        }

        if ( "".equals( errorMessage ) )
        {
            // TODO 登録済idチェック
            // // user_id の登録状況をチェックする。
            // Connection dbck = DBConnection.getConnection();
            // query = "SELECT * FROM hh_user_basic WHERE (user_id='" + user_id + "'";
            // query = query + " OR handle_name ='" + user_id + "')";
            // prestate = dbck.prepareStatement( query );
            // ResultSet resultck = prestate.executeQuery();
            // if( resultck.next() != false )
            // {
            // if(mail_addr_mobile.compareTo("") == 0 || resultck.getString("mail_addr_mobile").compareTo(mail_addr_mobile) != 0)
            // {
            // error_t[0] = 3; //ﾕｰｻﾞｰID登録済み;
            // }
            // }
            // DBConnection.releaseResources( resultck, prestate, dbck );
        }

        if ( "".equals( errorMessage ) )
        {
            // TODO NGワードチェック
            // Connection dbngword = DBConnection.getConnection();
            // query = "SELECT * FROM bbs_ngword WHERE hotel_id='hotenavi'";
            // prestate = dbngword.prepareStatement( query );
            // ResultSet resultngword = prestate.executeQuery();
            //
            // while( resultngword.next() != false )
            // {
            // ng_word = resultngword.getString("ng_word");
            // if (user_id.indexOf(ng_word) != -1)
            // {
            // error_t[0] = 4; //NGword
            // break;
            // }
            // }
            // DBConnection.releaseResources( resultngword, prestate, dbngword );
        }
        return errorMessage;

    }

    /**
     * 名前チェック
     * 
     * @param name
     * @return errorMessage
     */
    public static String nameValidationCheck(String name, boolean allowNull)
    {
        String errorMessage = "";
        if ( !isValidParameter( name ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[NAME][UNINPUTTED_ERR];
            }
        }
        else if ( CheckString.onlySpaceCheck( name ) )
        { // スペースのみの場合
            errorMessage = errorMessageTable[NAME][SPACE_ERR];
        }
        // else if (!fullWidthCheck(name))
        // { // 全角チェック
        // errorMessage = errorMessageTable[NAME][STRING_TYPE__ERR];
        // }

        else if ( !LengthCheck( name, NAME_ABOVE, NAME_FOLLOW ) )
        { // 文字数が指定範囲以外の場合
            errorMessage = errorMessageTable[NAME][NUM_OF_CHARA_ERR];
        }

        if ( "".equals( errorMessage ) )
        {
            // TODO NGワード
        }
        return errorMessage;
    }

    /**
     * パスワードチェック
     * 
     * @param password
     * @param password_ck
     * @param userId
     * @return errorMessage
     */
    public static String passwordValidationCheck(String password, String password_ck, String userId, boolean allowNull)
    {
        String errorMessage = "";
        if ( !isValidParameter( password ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[PASSWORD][UNINPUTTED_ERR];
            }
        }
        else if ( !numAlphaCheck( password ) )
        { // 半角英数以外は受け付けない
            errorMessage = errorMessageTable[PASSWORD][STRING_TYPE__ERR];
        }
        else if ( !LengthCheck( password, PASSWORD_ABOVE, PASSWORD_FOLLOW ) )
        { // 文字数が指定範囲以外の場合
            errorMessage = errorMessageTable[PASSWORD][NUM_OF_CHARA_ERR];
        }
        else if ( password.equals( userId ) )
        { // userIdと同一のパスワードは受け付けない
            errorMessage = errorMessageTable[PASSWORD][REGIST_ERR];
        }
        else if ( password_ck.compareTo( password ) != 0 )
        { // パスワード(確認用)との比較
            errorMessage = errorMessageTable[PASSWORD][COMPARE_ERR];
        }
        return errorMessage;
    }

    /**
     * パスワードチェック(ステイコンシェルジュ)
     * 
     * @param password
     * @param password_ck
     * @param userId
     * @return errorMessage
     */
    public static String passwordValidationCheckSC(String password, String password_ck, boolean allowNull)
    {
        String errorMessage = "";
        if ( !isValidParameter( password ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[PASSWORD][UNINPUTTED_ERR];
            }
        }
        else if ( !numAlphaCheck( password ) )
        { // 半角英数以外は受け付けない
            errorMessage = errorMessageTable[PASSWORD][STRING_TYPE__ERR];
        }
        else if ( !LengthCheck( password, PASSWORD_ABOVE_SC, PASSWORD_FOLLOW_SC ) )
        { // 文字数が指定範囲以外の場合
            errorMessage = errorMessageTable[PASSWORD][NUM_OF_CHARA_ERR_SC];
        }
        else if ( password_ck.compareTo( password ) != 0 )
        { // パスワード(確認用)との比較
            errorMessage = errorMessageTable[PASSWORD][COMPARE_ERR];
        }
        return errorMessage;
    }

    /**
     * フリガナチェック
     * 
     * @param furigana
     * @return errorMessage
     */
    public static String furiganaValidationCheck(String furigana, boolean allowNull)
    {
        // halfWidthKatakanaCheck
        String tempFurigana = "";
        String kanaHalfString = "";
        tempFurigana = ReplaceString.HTMLEscape( furigana );
        kanaHalfString = replaceKanaHalf( tempFurigana ).replace( " ", "" ).replace( "　", "" ).replace( "&#160;", "" );
        Logging.info( "kanaHalfString:" + kanaHalfString );
        String errorMessage = "";
        if ( !isValidParameter( tempFurigana ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[FURIGANA][UNINPUTTED_ERR];
            }
        }
        // else if ( !halfWidthKatakanaCheck( kanaHalfString ) )
        // { // 全角カタカナ⇒半角カナに変換後、半角カナチェック
        // errorMessage = errorMessageTable[FURIGANA][STRING_TYPE__ERR];
        // }
        else if ( !LengthCheck( kanaHalfString, FURIGANA_ABOVE, FURIGANA_FOLLOW ) )
        { // 文字数が指定範囲以外の場合
            errorMessage = errorMessageTable[FURIGANA][NUM_OF_CHARA_ERR];
        }

        if ( "".equals( errorMessage ) )
        {

        }
        return errorMessage;
    }

    /**
     * 性別チェック
     * 
     * @param sex
     * @return
     */
    public static String sexValidationCheck(String sex, boolean allowNull)
    {
        String errorMessage = "";
        if ( !isValidParameter( sex ) || "0".equals( sex ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[SEX][UNINPUTTED_ERR];
            }
        }
        return errorMessage;
    }

    /**
     * ニックネームチェック
     * 
     * @param handleName
     * @return errorMessage
     */
    public static String handleNameValidationCheck(String handleName, boolean allowNull)
    {
        String errorMessage = "";
        String tmpHandleName = "";
        tmpHandleName = ReplaceString.HTMLEscape( handleName );

        if ( !isValidParameter( handleName ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[HANDLE_NAME][UNINPUTTED_ERR];
            }
        }
        else if ( CheckString.onlySpaceCheck( handleName ) )
        { // スペースのみの場合
            errorMessage = errorMessageTable[HANDLE_NAME][SPACE_ERR];
        }
        // else if ( !fullWidthCheck( handleName ) )
        // { // 全角チェック
        // errorMessage = errorMessageTable[HANDLE_NAME][STRING_TYPE__ERR];
        // }
        else if ( !LengthCheck( tmpHandleName, HANDLENAME_ABOVE, HANDLENAME_FOLLOW ) )
        { // 文字数が指定範囲外の場合
            System.out.println( "ニックネーム" );
            errorMessage = errorMessageTable[HANDLE_NAME][NUM_OF_CHARA_ERR];
        }

        if ( "".equals( errorMessage ) && !"".equals( handleName ) )
        {
            // TODO ニックネーム登録済
            // UserBasicHistoryInfo ubhi = new UserBasicHistoryInfo( );
            // if(ubhi.getHandleNameCheck(user_id, handle_name))
            // {
            // error_t[3] = 2; //ﾆｯｸﾈｰﾑ登録済み;
            // }

        }

        if ( "".equals( errorMessage ) && !"".equals( handleName ) )
        {
            // TODO NGワードチェック
            // if( CheckNgWord.ngWordCheck( handleName ) != false )
            // {
            // error_t[3] = 3; //NGword
            // }

        }
        return errorMessage;
    }

    /**
     * 日付チェック
     * 
     * @param columnName
     * @param date
     * @return errorMessage
     */
    public static String dateValidationCheck(int columnName, String year, String month, String day, boolean allowNull)
    {
        String errorMessage = "";

        String date = null;
        switch( columnName % 3 )
        {
            case 1:
                date = year;
                break;
            case 2:
                date = month;
                break;
            default:
                date = day;
        }

        if ( !selectTagValidationCheck( date ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[columnName][UNINPUTTED_ERR];
            }
            else if ( month != null && day != null )
            {
                if ( columnName % 3 == 2 )// 月
                {
                    if ( Integer.parseInt( day ) != 0 && Integer.parseInt( month ) == 0 )
                    {
                        errorMessage = errorMessageTable[columnName][UNINPUTTED_ERR];
                    }
                }
                else if ( columnName % 3 == 0 ) // 日
                {
                    if ( Integer.parseInt( day ) == 0 && Integer.parseInt( month ) != 0 )
                    {
                        errorMessage = errorMessageTable[columnName][UNINPUTTED_ERR];
                    }
                }
            }
        }
        else
        {
            if ( columnName % 3 == 0 ) // 日
            {
                if ( Integer.parseInt( day ) != 0 && Integer.parseInt( month ) == 0 )
                {
                    // 月のエラーで表示済みなので判断しない
                }
                else if ( year != null && month != null && day != null )
                {
                    if ( Integer.parseInt( year ) == 0 )
                    {
                        year = "2000";
                    }
                    if ( DateEdit.checkDate( Integer.parseInt( year ), Integer.parseInt( month ), Integer.parseInt( day ) ) == false )
                    {
                        errorMessage = errorMessageTable[columnName][REGIST_ERR];
                    }
                }
                else if ( month != null && day != null )
                {
                    if ( DateEdit.checkDate( 2000, Integer.parseInt( month ), Integer.parseInt( day ) ) == false )
                    {
                        errorMessage = errorMessageTable[columnName][REGIST_ERR];
                    }
                }
            }
        }
        return errorMessage;
    }

    /**
     * 住所チェック
     * 
     * @param columnName
     * @param date
     * @return errorMessage
     */
    public static String addressValidationCheck(int columnName, String date, boolean allowNull)
    {
        String errorMessage = "";
        if ( !selectTagValidationCheck( date ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[columnName][UNINPUTTED_ERR];
            }
        }
        else
        {
            return errorMessage;
        }
        return errorMessage;
    }

    /**
     * 電話番号チェック
     * 
     * @param columnName
     * @param telNo
     * @return errorMessage
     */
    public static String telNoValidationCheck(int columnName, String telNo, boolean allowNull)
    {
        String errorMessage = "";
        if ( !isValidParameter( telNo ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[columnName][UNINPUTTED_ERR];
            }
        }
        else if ( !numCheck( telNo ) )
        { // 半角数字以外受け付けない
            errorMessage = errorMessageTable[columnName][STRING_TYPE__ERR];
        }
        else if ( !LengthCheck( telNo, TELLNUMBER_ABOVE, TELLNUMBER_FOLLOW ) )
        { // 文字数が指定範囲外の場合
            errorMessage = errorMessageTable[columnName][NUM_OF_CHARA_ERR];
        }
        return errorMessage;
    }

    /**
     * メールチェック
     * 
     * @param mailAdd
     * @param mailAdd_ck
     * @return errorMessage
     */
    public static String mailValidationCheck(String mailAdd, String mailAdd_ck, boolean allowNull)
    {
        String errorMessage = "";
        if ( !isValidParameter( mailAdd ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[MAIL_ADDRESS][UNINPUTTED_ERR];
            }
        }
        else if ( !mailaddrCheck( mailAdd ) )
        { // メルアドフォーマットチェック
            errorMessage = errorMessageTable[MAIL_ADDRESS][MAIL_FORMAT_ERR];
        }

        if ( "".equals( errorMessage ) )
        {
            // TODO メルアド重複チェック
            // メールアドレスの登録状況をチェックする。(メールアドレスが一致かつユーザIDが一致しないデータ)
            // Connection dbck_mail = DBConnection.getConnection();
            // query = "SELECT * FROM hh_user_basic WHERE mail_addr='" + mail_addr + "'";
            // query = query + " AND user_id !='" + user_id + "'";
            // query = query + " AND del_flag=0";
            // query = query + " UNION SELECT * FROM hh_user_basic WHERE mail_addr_mobile='" + mail_addr + "'";
            // query = query + " AND user_id !='" + user_id + "'";
            // query = query + " AND del_flag=0";
            // prestate = dbck_mail.prepareStatement( query );
            // ResultSet resultck_mail = prestate.executeQuery();
            // if( resultck_mail.next() != false )
            // {
            // if(resultck_mail.getInt("regist_status") == 9)
            // {
            // error_t[4] = 3;
            // }
            // else
            // {
            // mail_ck = 1;
            // }
            // }
            // DBConnection.releaseResources( resultck_mail, prestate, dbck_mail );
            //
            // // メールアドレスの登録状況をチェックする。(メールアドレスが一致してかつユーザIDが一致するデータ)
            // Connection dbck_status = DBConnection.getConnection();
            // query = "SELECT * FROM hh_user_basic WHERE mail_addr='" + mail_addr + "'";
            // query = query + " AND user_id ='" + user_id + "'";
            // query = query + " AND del_flag=0";
            // query = query + " UNION SELECT * FROM hh_user_basic WHERE mail_addr_mobile='" + mail_addr + "'";
            // query = query + " AND user_id ='" + user_id + "'";
            // query = query + " AND del_flag=0";
            // prestate = dbck_status.prepareStatement( query );
            // ResultSet resultck_status = prestate.executeQuery();
            // if( resultck_status.next() != false )
            // {
            // int regist_status = resultck_status.getInt("regist_status");
            // int temp_date_pc = resultck_status.getInt("temp_date_pc");
            // int temp_time_pc = resultck_status.getInt("temp_time_pc");
            // if (regist_status == 9)
            // {
            // error_t[4] = 5;
            // } //メールアドレス登録済み
            // else if((temp_date_pc == nowdate) || (temp_date_pc == predate && temp_time_pc > nowtime ))
            // {
            // error_kari = 1;
            // error_t[4] = 4;
            // } //仮登録済み
            // }
            // DBConnection.releaseResources( resultck_status, prestate, dbck_status );
            //
        }

        if ( "".equals( errorMessage ) )
        {
            if ( !mailAdd.equals( mailAdd_ck ) )
            {
                errorMessage = errorMessageTable[MAIL_ADDRESS][COMPARE_ERR];
            }
        }
        return errorMessage;
    }

    /**
     * メルマガ登録チェック
     * 
     * @param mailmag
     * @return errorMessage
     */
    public static String mailmagValidationCheck(String mailmag, boolean allowNull)
    {
        String errorMessage = "";
        if ( !isValidParameter( mailmag ) || "0".equals( mailmag ) )
        { // 未入力の場合
            if ( !allowNull ) // 入力必須
            {
                errorMessage = errorMessageTable[MAIL_MAGAZINE][UNINPUTTED_ERR];
            }
        }
        return errorMessage;
    }

    /**
     * 半角カタカナチェック
     * 
     * @param target チェック対象文字列
     * @return 処理結果(true:正常,false:異常)
     */
    private static boolean halfWidthKatakanaCheck(String target)
    {
        if ( Pattern.matches( "^[｡-ﾟ+]+$", target ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 全角チェック
     * 
     * @param target チェック対象文字列
     * @return 処理結果(true:正常,false:異常)
     */
    public static boolean fullWidthCheck(String target)
    {
        if ( Pattern.matches( "^[^ -~｡-ﾟ]+$", target ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /***
     * 文字列中の文字数を半角文字基準でチェックする
     * 
     * @param input チェック対象の文字列
     * @param length 入力可能長さ最小値
     * @param length 入力可能長さ最大値
     * @return チェック結果。
     **/
    private static boolean LengthCheck(String target, int start, int end)
    {
        int valueLeng;

        try
        {
            valueLeng = target.getBytes( "Windows-31J" ).length;
            System.out.println( "文字列チェックvalueLeng:" + valueLeng );
            if ( valueLeng < start || valueLeng > end )
            {
                // 桁数範囲エラー
                return false;
            }
        }
        catch ( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * selectTag入力チェック
     * 
     * @param target
     * @return
     */
    private static boolean selectTagValidationCheck(String target)
    {
        if ( isValidParameter( target ) && 0 != Integer.parseInt( target ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
