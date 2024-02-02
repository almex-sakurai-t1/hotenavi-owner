package jp.happyhotel.util;

import java.util.Date;

import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHhRsvSystemConf;

import org.apache.commons.lang.StringUtils;

import com.nimbusds.jwt.JWTClaimsSet;

/**
 * 予約情報トークン処理クラス<br>
 * <br>
 * 予約情報トークン関連の機能をまとめたユーテリティクラスです。<br>
 * 予約情報トークンの発行や検証の機能を提供します。<br>
 * <br>
 * トークンの秘密鍵はhh_rsv_system_confに登録した値を使用してください。(ctrl_id1=15, ctrl_id2=1)<br>
 * <br>
 * 例：<br>
 * &emsp;{@code reserveTokenSecretKey=<256-bit (32-byte) string>}<br>
 * 
 * @author mitsuhashi-k1
 */
public final class ReserveToken
{
    /** 共有秘密鍵 */
    private static String       secretKey        = getSecretKey();

    /** トークン発行者情報 */
    private static final String ISSUER           = "happyhotel.jp";

    /** JWTクレーム用文字列（ホテルID） */
    public static final String  ID_CLAIM         = "id";
    /** JWTクレーム用文字列（予約番号） */
    public static final String  RESERVE_NO_CLAIM = "reserve_no";
    /** JWTクレーム用文字列（ユーザID） */
    public static final String  USER_ID_CLAIM    = "user_id";

    /**
     * コンストラクタ
     */
    private ReserveToken()
    {
    }

    /**
     * 予約情報トークン発行<br>
     * <br>
     * 現在時刻から指定時間だけ有効な予約情報トークンを発行します。<br>
     * 予約情報トークンには、発行者、発行日時、有効期限、ホテルID、予約番号、ユーザIDが格納されます。<br>
     * 
     * @param id ホテルID
     * @param reserveNo 予約番号
     * @param userId ユーザID
     * @param minute トークンを有効にする期間（分）
     * @return 予約情報トークン
     * @throws IllegalArgumentException JWSオブジェクトに署名できなかった場合
     */
    public static String issue(int id, String reserveNo, String userId)
    {
        Date nowDate = new Date();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer( ISSUER )
                .issueTime( nowDate )
                .claim( ID_CLAIM, id )
                .claim( RESERVE_NO_CLAIM, reserveNo )
                .claim( USER_ID_CLAIM, userId )
                .build();

        return JsonWebToken.issue( claimsSet, secretKey );
    }

    /**
     * 予約情報トークン検証<br>
     * <br>
     * 下記項目により、予約情報トークンを検証します。<br>
     * ・正しい秘密鍵でエンコードされているかどうか<br>
     * ・発行者が正しく設定されているかどうか<br>
     * ・発行日時が設定されているかどうか<br>
     * ・有効期限が設定されているかどうか（有効期限内かどうかは検証対象外）<br>
     * ・ユーザIDが設定されているかどうか<br>
     * 
     * @param token 予約情報トークン
     * @return 検証結果を示すブール値
     */
    public static boolean verify(String token)
    {
        // 秘密鍵による検証
        Logging.info( "秘密鍵による検証! secretKey=" + secretKey );
        if ( !JsonWebToken.verifyBySecretKey( token, secretKey ) )
        {
            return false;
        }
        Logging.info( "ハピホテが発行したトークンかどうかの検証 ISSUER=" + ISSUER );
        // ハピホテが発行したトークンかどうかの検証
        if ( !JsonWebToken.verifyByIssuer( token, ISSUER ) )
        {
            return false;
        }

        // ホテルIDが設定されているかどうかの検証
        if ( !verifyById( token ) )
        {
            return false;
        }
        // 予約番号が設定されているかどうかの検証
        if ( !verifyByReserveNo( token ) )
        {
            return false;
        }
        // ユーザIDが設定されているかどうかの検証
        if ( !verifyByUserId( token ) )
        {
            return false;
        }
        return true;
    }

    /**
     * 有効期限判定 <br>
     * <br>
     * 予約情報トークンの有効期限を判定します。<br>
     * 予約情報トークンが正しいものかどうかは検証されません。<br>
     * 有効期限が設定されていない、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前に予約情報トークンの検証を行ってください。<br>
     * 
     * @param token 予約情報トークン
     * @return 予約情報トークンが有効期限内かどうかを示すブール値
     * @throws IllegalArgumentException 予約情報トークンに有効期限が設定されていない、もしくは取得に失敗した場合
     */
    public static boolean isWithinExpirationTime(String token)
    {
        return JsonWebToken.isWithinExpirationTime( token );
    }

    /**
     * ホテルID取得<br>
     * <br>
     * 予約情報トークンに格納されているホテルIDを取得します。<br>
     * 予約情報トークンが正しいものかどうかは検証されません。<br>
     * ホテルIDが格納されていない（空文字を含む）、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前に予約情報トークンの検証を行ってください。<br>
     * 
     * @param token 予約情報トークン
     * @return ホテルID
     * @throws IllegalArgumentException 予約情報トークンにホテルIDが設定されていない、もしくは取得に失敗した場合
     */
    public static Integer getId(String token)
    {
        Integer id = JsonWebToken.getIntegerClaim( token, ID_CLAIM );

        if ( id == null )
        {
            throw new IllegalArgumentException( "tokenにはホテルIDが設定されていません。" );
        }

        return id;
    }

    /**
     * 予約番号取得<br>
     * <br>
     * 予約情報トークンに格納されている予約番号を取得します。<br>
     * 予約情報トークンが正しいものかどうかは検証されません。<br>
     * 予約番号が格納されていない（空文字を含む）、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前に予約情報トークンの検証を行ってください。<br>
     * 
     * @param token 予約情報トークン
     * @return 予約番号
     * @throws IllegalArgumentException 予約情報トークンに予約番号が設定されていない、もしくは取得に失敗した場合
     */
    public static String getReserveNo(String token)
    {
        String reserveNo = JsonWebToken.getStringClaim( token, RESERVE_NO_CLAIM );

        if ( StringUtils.isEmpty( reserveNo ) )
        {
            throw new IllegalArgumentException( "tokenには予約番号が設定されていません。" );
        }

        return reserveNo;
    }

    /**
     * ユーザID取得<br>
     * <br>
     * 予約情報トークンに格納されているユーザIDを取得します。<br>
     * 予約情報トークンが正しいものかどうかは検証されません。<br>
     * ユーザIDが格納されていない（空文字を含む）、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前に予約情報トークンの検証を行ってください。<br>
     * 
     * @param token 予約情報トークン
     * @return ユーザID
     * @throws IllegalArgumentException 予約情報トークンにユーザIDが設定されていない、もしくは取得に失敗した場合
     */
    public static String getUserId(String token)
    {
        String userId = JsonWebToken.getStringClaim( token, USER_ID_CLAIM );

        if ( StringUtils.isEmpty( userId ) )
        {
            throw new IllegalArgumentException( "tokenにはユーザIDが設定されていません。" );
        }

        return userId;
    }

    /**
     * 予約情報トークン検証（ホテルID）<br>
     * <br>
     * 予約情報トークンにホテルIDが設定されているかどうかを検証します。<br>
     * ホテルIDがnullもしくは空文字の場合、設定されていないものと判定されます。<br>
     * 
     * @param token 予約情報トークン
     * @return 予約情報トークンにホテルIDが設定されているかどうかを示すブール値
     */
    public static boolean verifyById(String token)
    {
        try
        {
            Integer id = JsonWebToken.getIntegerClaim( token, ID_CLAIM );

            if ( id == null )
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
     * 予約情報トークン検証（予約番号）<br>
     * <br>
     * 予約情報トークンに予約番号が設定されているかどうかを検証します。<br>
     * 予約番号がnullもしくは空文字の場合、設定されていないものと判定されます。<br>
     * 
     * @param token 予約情報トークン
     * @return 予約情報トークンに予約番号が設定されているかどうかを示すブール値
     */
    public static boolean verifyByReserveNo(String token)
    {
        try
        {
            String reserveNo = JsonWebToken.getStringClaim( token, RESERVE_NO_CLAIM );

            if ( StringUtils.isEmpty( reserveNo ) )
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
     * 予約情報トークン検証（ユーザID）<br>
     * <br>
     * 予約情報トークンにユーザIDが設定されているかどうかを検証します。<br>
     * ユーザIDがnullもしくは空文字の場合、設定されていないものと判定されます。<br>
     * 
     * @param token 予約情報トークン
     * @return 予約情報トークンにユーザIDが設定されているかどうかを示すブール値
     */
    public static boolean verifyByUserId(String token)
    {
        try
        {
            String userId = JsonWebToken.getStringClaim( token, USER_ID_CLAIM );

            if ( StringUtils.isEmpty( userId ) )
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
