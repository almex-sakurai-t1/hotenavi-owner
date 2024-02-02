package jp.happyhotel.util;

import java.util.Date;

import jp.happyhotel.data.DataHhRsvSystemConf;

import com.nimbusds.jwt.JWTClaimsSet;

/**
 * ステイコンシェルジュ会員登録メール認証トークン処理クラス<br>
 * <br>
 * ステイコンシェルジュ会員登録メール認証トークン関連の機能をまとめたユーテリティクラスです。<br>
 * ステイコンシェルジュ会員登録メール認証トークンの発行や検証の機能を提供します。<br>
 * <br>
 * トークンの秘密鍵はhh_rsv_system_confに登録した値を使用してください。(ctrl_id1=15, ctrl_id2=1)<br>
 * <br>
 * 例：<br>
 * &emsp;{@code reserveTokenSecretKey=<256-bit (32-byte) string>}<br>
 * 
 * @author mitsuhashi-k1
 */
public final class StayConciergeMemberMailToken
{
    /** 共有秘密鍵 */
    private static String       secretKey       = getSecretKey();

    /** トークン発行者情報 */
    private static final String ISSUER          = "happyhotel.jp";

    /** JWTクレーム用文字列（md5） */
    public static final String  MD5_CLAIM       = "md5";
    /** JWTクレーム用文字列（ホテルID） */
    public static final String  HOTEL_ID_CLAIM  = "hotel_id";
    /** JWTクレーム用文字列（ユーザーID） */
    public static final String  USER_ID_CLAIM   = "user_id";
    /** JWTクレーム用文字列（カスタムID） */
    public static final String  CUSTOM_ID_CLAIM = "custom_id";
    /** JWTクレーム用文字列（誕生日1（月）） */
    public static final String  BIRTHDAY1_CLAIM = "birthday1";
    /** JWTクレーム用文字列（誕生日2（日）） */
    public static final String  BIRTHDAY2_CLAIM = "birthday2";

    /**
     * コンストラクタ
     */
    private StayConciergeMemberMailToken()
    {
    }

    /**
     * ステイコンシェルジュ会員登録メール認証トークン発行<br>
     * <br>
     * 現在時刻から指定時間だけ有効なステイコンシェルジュ会員登録メール認証トークンを発行します。<br>
     * ステイコンシェルジュ会員登録メール認証トークンには、発行者、発行日時、有効期限、md5、ユーザーIDが格納されます。<br>
     * 
     * @param md5 m_member.md5
     * @param userId ユーザーID
     * @param customId カスタムID
     * @param birthday1 誕生日1(月)
     * @param birthday2 誕生日2（日）
     * @param minute トークンを有効にする期間（分）
     * @return ステイコンシェルジュ会員登録メール認証トークン
     * @throws IllegalArgumentException JWSオブジェクトに署名できなかった場合
     */
    public static String issue(String md5, String hotelId, String userId, String customId, String birthday1, String birthday2)
    {
        Date nowDate = new Date();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer( ISSUER )
                .issueTime( nowDate )
                .claim( MD5_CLAIM, md5 )
                .claim( HOTEL_ID_CLAIM, hotelId )
                .claim( USER_ID_CLAIM, userId )
                .claim( CUSTOM_ID_CLAIM, customId )
                .claim( BIRTHDAY1_CLAIM, birthday1 )
                .claim( BIRTHDAY2_CLAIM, birthday2 )
                .build();

        return JsonWebToken.issue( claimsSet, secretKey );
    }

    /**
     * ステイコンシェルジュ会員登録メール認証情報トークン検証<br>
     * <br>
     * 下記項目により、ステイコンシェルジュ会員登録メール認証トークンを検証します。<br>
     * ・正しい秘密鍵でエンコードされているかどうか<br>
     * ・発行者が正しく設定されているかどうか<br>
     * ・発行日時が設定されているかどうか<br>
     * ・有効期限が設定されているかどうか（有効期限内かどうかは検証対象外）<br>
     * ・ユーザIDが設定されているかどうか<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return 検証結果を示すブール値
     */
    public static boolean verify(String token)
    {
        // 秘密鍵による検証
        if ( !JsonWebToken.verifyBySecretKey( token, secretKey ) )
        {
            return false;
        }

        // ハピホテが発行したトークンかどうかの検証
        if ( !JsonWebToken.verifyByIssuer( token, ISSUER ) )
        {
            return false;
        }

        // md5が設定されているかどうかの検証
        if ( !verifyByMd5( token ) )
        {
            return false;
        }

        // ホテルIDが設定されているかどうかの検証
        if ( !verifyByHotelId( token ) )
        {
            return false;
        }

        // ユーザーIDが設定されているかどうかの検証
        if ( !verifyByUserId( token ) )
        {
            return false;
        }

        // カスタムIDが設定されているかどうかの検証
        if ( !verifyByCustomId( token ) )
        {
            return false;
        }

        // 誕生日1が設定されているかどうかの検証
        if ( !verifyByBirthday1( token ) )
        {
            return false;
        }

        // 誕生日2が設定されているかどうかの検証
        if ( !verifyByBirthday2( token ) )
        {
            return false;
        }

        return true;
    }

    /**
     * 有効期限判定 <br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンの有効期限を判定します。<br>
     * ステイコンシェルジュ会員登録メール認証トークンが正しいものかどうかは検証されません。<br>
     * 有効期限が設定されていない、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前にステイコンシェルジュ会員登録メール認証トークンの検証を行ってください。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return ステイコンシェルジュ会員登録メール認証トークンが有効期限内かどうかを示すブール値
     * @throws IllegalArgumentException ステイコンシェルジュ会員登録メール認証トークンに有効期限が設定されていない、もしくは取得に失敗した場合
     */
    public static boolean isWithinExpirationTime(String token)
    {
        return JsonWebToken.isWithinExpirationTime( token );
    }

    /**
     * md5取得<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンに格納されているmd5を取得します。<br>
     * ステイコンシェルジュ会員登録メール認証トークンが正しいものかどうかは検証されません。<br>
     * md5が格納されていない、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前にステイコンシェルジュ会員登録メール認証トークンの検証を行ってください。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return md5
     * @throws IllegalArgumentException ステイコンシェルジュ会員登録メール認証トークンにmd5が設定されていない、もしくは取得に失敗した場合
     */
    public static String getMd5(String token)
    {
        String md5 = JsonWebToken.getStringClaim( token, MD5_CLAIM );

        if ( md5 == null )
        {
            throw new IllegalArgumentException( "tokenにはMD5が設定されていません。" );
        }

        return md5;
    }

    /**
     * ホテルID取得<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンに格納されているホテルIDを取得します。<br>
     * ステイコンシェルジュ会員登録メール認証トークンが正しいものかどうかは検証されません。<br>
     * ホテルIDが格納されていない、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前にステイコンシェルジュ会員登録メール認証トークンの検証を行ってください。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return ホテルID
     * @throws IllegalArgumentException ステイコンシェルジュ会員登録メール認証トークンにユーザーIDが設定されていない、もしくは取得に失敗した場合
     */
    public static String getHotelId(String token)
    {
        String userId = JsonWebToken.getStringClaim( token, HOTEL_ID_CLAIM );

        if ( userId == null )
        {
            throw new IllegalArgumentException( "tokenにはホテルIDが設定されていません。" );
        }

        return userId;
    }

    /**
     * ユーザーID取得<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンに格納されているユーザーIDを取得します。<br>
     * ステイコンシェルジュ会員登録メール認証トークンが正しいものかどうかは検証されません。<br>
     * ユーザーIDが格納されていない、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前にステイコンシェルジュ会員登録メール認証トークンの検証を行ってください。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return ユーザーID
     * @throws IllegalArgumentException ステイコンシェルジュ会員登録メール認証トークンにユーザーIDが設定されていない、もしくは取得に失敗した場合
     */
    public static String getUserId(String token)
    {
        String userId = JsonWebToken.getStringClaim( token, USER_ID_CLAIM );

        if ( userId == null )
        {
            throw new IllegalArgumentException( "tokenにはユーザーIDが設定されていません。" );
        }

        return userId;
    }

    /**
     * カスタムID取得<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンに格納されているカスタムIDを取得します。<br>
     * ステイコンシェルジュ会員登録メール認証トークンが正しいものかどうかは検証されません。<br>
     * カスタムIDが格納されていない、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前にステイコンシェルジュ会員登録メール認証トークンの検証を行ってください。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return カスタムID
     * @throws IllegalArgumentException ステイコンシェルジュ会員登録メール認証トークンにユーザーIDが設定されていない、もしくは取得に失敗した場合
     */
    public static String getCustomId(String token)
    {
        String customId = JsonWebToken.getStringClaim( token, CUSTOM_ID_CLAIM );

        if ( customId == null )
        {
            throw new IllegalArgumentException( "tokenにはカスタムIDが設定されていません。" );
        }

        return customId;
    }

    /**
     * 誕生日1取得<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンに格納されている誕生日1を取得します。<br>
     * ステイコンシェルジュ会員登録メール認証トークンが正しいものかどうかは検証されません。<br>
     * 誕生日1が格納されていない、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前にステイコンシェルジュ会員登録メール認証トークンの検証を行ってください。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return 誕生日1
     * @throws IllegalArgumentException ステイコンシェルジュ会員登録メール認証トークンにユーザーIDが設定されていない、もしくは取得に失敗した場合
     */
    public static String getBirthday1(String token)
    {
        String birthday1 = JsonWebToken.getStringClaim( token, BIRTHDAY1_CLAIM );

        if ( birthday1 == null )
        {
            throw new IllegalArgumentException( "tokenには誕生日1が設定されていません。" );
        }

        return birthday1;
    }

    /**
     * 誕生日2取得<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンに格納されている誕生日2を取得します。<br>
     * ステイコンシェルジュ会員登録メール認証トークンが正しいものかどうかは検証されません。<br>
     * 誕生日2が格納されていない、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前にステイコンシェルジュ会員登録メール認証トークンの検証を行ってください。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return 誕生日2
     * @throws IllegalArgumentException ステイコンシェルジュ会員登録メール認証トークンにユーザーIDが設定されていない、もしくは取得に失敗した場合
     */
    public static String getBirthday2(String token)
    {
        String birthday2 = JsonWebToken.getStringClaim( token, BIRTHDAY2_CLAIM );

        if ( birthday2 == null )
        {
            throw new IllegalArgumentException( "tokenには誕生日2が設定されていません。" );
        }

        return birthday2;
    }

    /**
     * ステイコンシェルジュ会員登録メール認証トークン検証（md5）<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンにmd5が設定されているかどうかを検証します。<br>
     * md5がnullの場合、設定されていないものと判定されます。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return ステイコンシェルジュ会員登録メール認証トークンにmd5が設定されているかどうかを示すブール値
     */
    public static boolean verifyByMd5(String token)
    {
        try
        {
            String md5 = JsonWebToken.getStringClaim( token, MD5_CLAIM );

            if ( md5 == null )
            {
                return false;
            }

            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * ステイコンシェルジュ会員登録メール認証トークン検証(ホテルID）<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンにユーザーIDが設定されているかどうかを検証します。<br>
     * ホテルIDがnullの場合、設定されていないものと判定されます。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return ステイコンシェルジュ会員登録メール認証トークンにホテルIDが設定されているかどうかを示すブール値
     */
    public static boolean verifyByHotelId(String token)
    {
        try
        {
            String hotelId = JsonWebToken.getStringClaim( token, HOTEL_ID_CLAIM );

            if ( hotelId == null )
            {
                return false;
            }

            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * ステイコンシェルジュ会員登録メール認証トークン検証(ユーザーID）<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンにユーザーIDが設定されているかどうかを検証します。<br>
     * ユーザーIDがnullの場合、設定されていないものと判定されます。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return ステイコンシェルジュ会員登録メール認証トークンにユーザーIDが設定されているかどうかを示すブール値
     */
    public static boolean verifyByUserId(String token)
    {
        try
        {
            String userId = JsonWebToken.getStringClaim( token, USER_ID_CLAIM );

            if ( userId == null )
            {
                return false;
            }

            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * ステイコンシェルジュ会員登録メール認証トークン検証(カスタムID）<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンにカスタムIDが設定されているかどうかを検証します。<br>
     * カスタムIDがnullの場合、設定されていないものと判定されます。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return ステイコンシェルジュ会員登録メール認証トークンにカスタムIDが設定されているかどうかを示すブール値
     */
    public static boolean verifyByCustomId(String token)
    {
        try
        {
            String customId = JsonWebToken.getStringClaim( token, CUSTOM_ID_CLAIM );

            if ( customId == null )
            {
                return false;
            }

            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * ステイコンシェルジュ会員登録メール認証トークン検証(誕生日1）<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンに誕生日1が設定されているかどうかを検証します。<br>
     * 誕生日1がnullの場合、設定されていないものと判定されます。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return ステイコンシェルジュ会員登録メール認証トークンに誕生日1が設定されているかどうかを示すブール値
     */
    public static boolean verifyByBirthday1(String token)
    {
        try
        {
            String birthday1 = JsonWebToken.getStringClaim( token, BIRTHDAY1_CLAIM );

            if ( birthday1 == null )
            {
                return false;
            }

            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * ステイコンシェルジュ会員登録メール認証トークン検証(誕生日2）<br>
     * <br>
     * ステイコンシェルジュ会員登録メール認証トークンに誕生日2が設定されているかどうかを検証します。<br>
     * 誕生日1がnullの場合、設定されていないものと判定されます。<br>
     * 
     * @param token ステイコンシェルジュ会員登録メール認証トークン
     * @return ステイコンシェルジュ会員登録メール認証トークンに誕生日2が設定されているかどうかを示すブール値
     */
    public static boolean verifyByBirthday2(String token)
    {
        try
        {
            String birthday2 = JsonWebToken.getStringClaim( token, BIRTHDAY2_CLAIM );

            if ( birthday2 == null )
            {
                return false;
            }

            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * 秘密鍵取得<br>
     * <br>
     * トークンの秘密鍵をhh_rsv_system_confから取得します。(ctrl_id1=15, ctrl_id2=1)<br>
     * 
     * @return 秘密鍵
     */
    private static String getSecretKey()
    {
        String secretKey = "";
        try
        {
            // 秘密鍵をhh_rsv_system_confから取得する。
            DataHhRsvSystemConf sysConf = new DataHhRsvSystemConf();
            if ( sysConf.getData( 15, 1 ) )
            {
                secretKey = sysConf.getVal2();
            }
            return secretKey;
        }
        catch ( Exception e )
        {
            return secretKey;
        }
    }
}
