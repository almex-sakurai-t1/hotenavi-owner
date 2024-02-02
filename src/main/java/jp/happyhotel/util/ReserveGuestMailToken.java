package jp.happyhotel.util;

import java.util.Date;

import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHhRsvSystemConf;

import com.nimbusds.jwt.JWTClaimsSet;

/**
 * ゲストメール認証トークン処理クラス<br>
 * <br>
 * ゲストメール認証トークン関連の機能をまとめたユーテリティクラスです。<br>
 * ゲストメール認証トークンの発行や検証の機能を提供します。<br>
 * <br>
 * トークンの秘密鍵はhh_rsv_system_confに登録した値を使用してください。(ctrl_id1=15, ctrl_id2=1)<br>
 * <br>
 * 例：<br>
 * &emsp;{@code reserveTokenSecretKey=<256-bit (32-byte) string>}<br>
 * 
 * @author mitsuhashi-k1
 */
public final class ReserveGuestMailToken
{
    /** 共有秘密鍵 */
    private static String       secretKey             = getSecretKey();

    /** トークン発行者情報 */
    private static final String ISSUER                = "happyhotel.jp";

    /** JWTクレーム用文字列（ホテルID） */
    public static final String  ID_CLAIM              = "id";
    /** JWTクレーム用文字列（仮予約番号） */
    public static final String  RESERVE_TEMP_NO_CLAIM = "reserve_temp_no";

    /**
     * コンストラクタ
     */
    private ReserveGuestMailToken()
    {
    }

    /**
     * ゲストメール認証トークン発行<br>
     * <br>
     * 現在時刻から指定時間だけ有効なゲストメール認証トークンを発行します。<br>
     * ゲストメール認証トークンには、発行者、発行日時、有効期限、ホテルID、仮予約番号が格納されます。<br>
     * 
     * @param id ホテルID
     * @param reserveTemoNo 仮予約番号
     * @param minute トークンを有効にする期間（分）
     * @return ゲストメール認証トークン
     * @throws IllegalArgumentException JWSオブジェクトに署名できなかった場合
     */
    public static String issue(int id, long reserveTempNo)
    {
        Date nowDate = new Date();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer( ISSUER )
                .issueTime( nowDate )
                .claim( ID_CLAIM, id )
                .claim( RESERVE_TEMP_NO_CLAIM, reserveTempNo )
                .build();

        return JsonWebToken.issue( claimsSet, secretKey );
    }

    /**
     * ゲストメール認証情報トークン検証<br>
     * <br>
     * 下記項目により、ゲストメール認証トークンを検証します。<br>
     * ・正しい秘密鍵でエンコードされているかどうか<br>
     * ・発行者が正しく設定されているかどうか<br>
     * ・発行日時が設定されているかどうか<br>
     * ・有効期限が設定されているかどうか（有効期限内かどうかは検証対象外）<br>
     * ・ユーザIDが設定されているかどうか<br>
     * 
     * @param token ゲストメール認証トークン
     * @return 検証結果を示すブール値
     */
    public static boolean verify(String token)
    {
        // 秘密鍵による検証
        Logging.info( "秘密鍵による検証! secretKey=" + secretKey, "verify" );
        if ( !JsonWebToken.verifyBySecretKey( token, secretKey ) )
        {
            return false;
        }

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

        // 仮予約番号が設定されているかどうかの検証
        if ( !verifyByReserveTempNo( token ) )
        {
            return false;
        }

        return true;
    }

    /**
     * 有効期限判定 <br>
     * <br>
     * ゲストメール認証トークンの有効期限を判定します。<br>
     * ゲストメール認証トークンが正しいものかどうかは検証されません。<br>
     * 有効期限が設定されていない、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前にゲストメール認証トークンの検証を行ってください。<br>
     * 
     * @param token ゲストメール認証トークン
     * @return ゲストメール認証トークンが有効期限内かどうかを示すブール値
     * @throws IllegalArgumentException ゲストメール認証トークンに有効期限が設定されていない、もしくは取得に失敗した場合
     */
    public static boolean isWithinExpirationTime(String token)
    {
        return JsonWebToken.isWithinExpirationTime( token );
    }

    /**
     * ホテルID取得<br>
     * <br>
     * ゲストメール認証トークンに格納されているホテルIDを取得します。<br>
     * ゲストメール認証トークンが正しいものかどうかは検証されません。<br>
     * ホテルIDが格納されていない（空文字を含む）、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前にゲストメール認証トークンの検証を行ってください。<br>
     * 
     * @param token ゲストメール認証トークン
     * @return ホテルID
     * @throws IllegalArgumentException ゲストメール認証トークンにホテルIDが設定されていない、もしくは取得に失敗した場合
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
     * 仮予約番号取得<br>
     * <br>
     * ゲストメール認証トークンに格納されている仮予約番号を取得します。<br>
     * ゲストメール認証トークンが正しいものかどうかは検証されません。<br>
     * 仮予約番号が格納されていない（空文字を含む）、もしくは取得に失敗した場合、IllegalArgumentExceptionを投げます。<br>
     * 例外の発生を回避したい場合は、{@link #verify(String token)}を用いて事前にゲストメール認証トークンの検証を行ってください。<br>
     * 
     * @param token ゲストメール認証トークン
     * @return 仮予約番号
     * @throws IllegalArgumentException ゲストメール認証トークンに仮予約番号が設定されていない、もしくは取得に失敗した場合
     */
    public static Long getReserveTempNo(String token)
    {
        Long reserveTempNo = JsonWebToken.getLongClaim( token, RESERVE_TEMP_NO_CLAIM );

        if ( reserveTempNo == null )
        {
            throw new IllegalArgumentException( "tokenには仮予約番号が設定されていません。" );
        }

        return reserveTempNo;
    }

    /**
     * ゲストメール認証トークン検証（ホテルID）<br>
     * <br>
     * ゲストメール認証トークンにホテルIDが設定されているかどうかを検証します。<br>
     * ホテルIDがnullもしくは空文字の場合、設定されていないものと判定されます。<br>
     * 
     * @param token ゲストメール認証トークン
     * @return ゲストメール認証トークンにホテルIDが設定されているかどうかを示すブール値
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
     * ゲストメール認証トークン検証(仮予約番号）<br>
     * <br>
     * ゲストメール認証トークンに仮予約番号が設定されているかどうかを検証します。<br>
     * 仮予約番号がnullもしくは空文字の場合、設定されていないものと判定されます。<br>
     * 
     * @param token ゲストメール認証トークン
     * @return ゲストメール認証トークンに仮予約番号が設定されているかどうかを示すブール値
     */
    public static boolean verifyByReserveTempNo(String token)
    {
        try
        {
            Long reserveTempNo = JsonWebToken.getLongClaim( token, RESERVE_TEMP_NO_CLAIM );

            if ( reserveTempNo == null )
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
