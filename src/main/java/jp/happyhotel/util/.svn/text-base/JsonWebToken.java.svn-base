package jp.happyhotel.util;

import java.text.ParseException;
import java.util.Date;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * JWT関連の機能をまとめたユーテリティクラスです。<br>
 * トークンの発行や検証の機能を提供します。<br>
 * 
 * @author koshiba-y1
 * @see <a href="https://connect2id.com/products/nimbus-jose-jwt/examples/jwt-with-hmac">JSON Web Token (JWT) with HMAC protection</a>
 */
public final class JsonWebToken
{
    private JsonWebToken()
    {
    }

    /**
     * トークンの発行。
     * 
     * @param claimsSet データを格納したJWTClaimsSetオブジェクト
     * @param secretKey トークンの秘密鍵
     * @return トークン
     * @throws NullPointerException 指定した秘密鍵が{@code null}だった場合
     * @throws IllegalArgumentException JWSオブジェクトに署名できなかった場合
     */
    public static String issue(JWTClaimsSet claimsSet, String secretKey)
    {
        if ( secretKey == null )
            throw new NullPointerException();

        SignedJWT signedJWT = new SignedJWT( new JWSHeader( JWSAlgorithm.HS256 ), claimsSet );

        try
        {
            JWSSigner signer = new MACSigner( secretKey.getBytes() );
            signedJWT.sign( signer );
        }
        catch ( JOSEException e )
        {
            throw new IllegalArgumentException( "トークンの発行に失敗しました。", e );
        }

        return signedJWT.serialize();
    }

    /**
     * JWTClaimsSetの抽出。
     * 
     * @param token トークン
     * @return JWTClaimsSetオブジェクト
     * @throws ParseException JWTのペイロードが、有効なJSONオブジェクトとJWTクレームセットを表していない場合
     */
    public static JWTClaimsSet extractClaimsSet(String token) throws ParseException
    {
        SignedJWT signedJWT = SignedJWT.parse( token );
        return signedJWT.getJWTClaimsSet();
    }

    /**
     * トークンの発行者の取得。
     * 
     * @param token トークン
     * @return トークンの発行者
     * @throws IllegalArgumentException トークンとして渡された文字列が有効なJWTストリングではない場合
     */
    public static String getIssuer(String token)
    {
        try
        {
            return extractClaimsSet( token ).getIssuer();
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "tokenは有効なJWT文字列ではありません。", e );
        }
    }

    /**
     * トークンの発行日時の取得。
     * 
     * @param token トークン
     * @return トークンの発行日時
     * @throws IllegalArgumentException トークンとして渡された文字列が有効なJWTストリングではない場合
     */
    public static Date getIssueTime(String token)
    {
        try
        {
            return extractClaimsSet( token ).getIssueTime();
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "tokenは有効なJWT文字列ではありません。", e );
        }
    }

    /**
     * トークンの有効期限の取得。
     * 
     * @param token トークン
     * @return トークンの有効期限
     * @throws IllegalArgumentException トークンとして渡された文字列が有効なJWTストリングではない場合
     */
    public static Date getExpirationTime(String token)
    {
        try
        {
            return extractClaimsSet( token ).getExpirationTime();
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "tokenは有効なJWT文字列ではありません。", e );
        }
    }

    /**
     * トークンのサブジェクトの取得。
     * 
     * @param token トークン
     * @return トークンのサブジェクト
     * @throws IllegalArgumentException トークンとして渡された文字列が有効なJWTストリングではない場合
     */
    public static String getSubject(String token)
    {
        try
        {
            return extractClaimsSet( token ).getSubject();
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "tokenは有効なJWT文字列ではありません。", e );
        }
    }

    /**
     * クレーム（文字列）の取得。
     * 
     * @param token トークン
     * @param claim クレーム名
     * @return クレームに格納された値
     * @throws IllegalArgumentException トークンとして渡された文字列が有効なJWTストリングではない場合
     */
    public static String getStringClaim(String token, String claim)
    {
        try
        {
            return extractClaimsSet( token ).getStringClaim( claim );
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "tokenは有効なJWT文字列ではありません。", e );
        }
    }

    /**
     * クレーム（数値）の取得。
     * 
     * @param token トークン
     * @param claim クレーム名
     * @return クレームに格納された値
     * @throws IllegalArgumentException トークンとして渡された文字列が有効なJWTストリングではない場合
     */
    public static Integer getIntegerClaim(String token, String claim)
    {
        try
        {
            return extractClaimsSet( token ).getIntegerClaim( claim );
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "tokenは有効なJWT文字列ではありません。", e );
        }
    }

    /**
     * クレーム（数値(long)）の取得。
     * 
     * @param token トークン
     * @param claim クレーム名
     * @return クレームに格納された値
     * @throws IllegalArgumentException トークンとして渡された文字列が有効なJWTストリングではない場合
     */
    public static Long getLongClaim(String token, String claim)
    {
        try
        {
            return extractClaimsSet( token ).getLongClaim( claim );
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "tokenは有効なJWT文字列ではありません。", e );
        }
    }

    /**
     * クレーム（ブール値）の取得。
     * 
     * @param token トークン
     * @param claim クレーム名
     * @return クレームに格納された値
     * @throws IllegalArgumentException トークンとして渡された文字列が有効なJWTストリングではない場合
     */
    public static Boolean getBooleanClaim(String token, String claim)
    {
        try
        {
            return extractClaimsSet( token ).getBooleanClaim( claim );
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "tokenは有効なJWT文字列ではありません。", e );
        }
    }

    /**
     * クレーム（日時）の取得。
     * 
     * @param token トークン
     * @param claim クレーム名
     * @return クレームに格納された値
     * @throws IllegalArgumentException トークンとして渡された文字列が有効なJWTストリングではない場合
     */
    public static Date getDateClaim(String token, String claim)
    {
        try
        {
            return extractClaimsSet( token ).getDateClaim( claim );
        }
        catch ( ParseException e )
        {
            throw new IllegalArgumentException( "tokenは有効なJWT文字列ではありません。", e );
        }
    }

    /**
     * トークンが指定した秘密鍵によってエンコードされているかどうかを検証します。
     * 
     * @param token トークン
     * @param secretKey トークンの秘密鍵
     * @return トークンが指定した秘密鍵によってエンコードされているかどうかを示すブール値
     * @throws NullPointerException 指定した秘密鍵が{@code null}だった場合
     */
    public static boolean verifyBySecretKey(String token, String secretKey)
    {
        if ( secretKey == null )
            throw new NullPointerException();

        try
        {
            SignedJWT signedJWT = SignedJWT.parse( token );

            JWSVerifier verifier = new MACVerifier( secretKey.getBytes() );

            return signedJWT.verify( verifier );
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * トークンに指定した発行者と等しい発行者が設定されているかどうかを検証します。
     * 
     * @param token トークン
     * @param issuer トークンの発行者
     * @return トークンの発行者が指定した発行者と等しいかどうかを示すブール値
     * @throws NullPointerException 指定したトークンの発行者が{@code null}だった場合
     */
    public static boolean verifyByIssuer(String token, String issuer)
    {
        if ( issuer == null )
            throw new NullPointerException();

        try
        {
            String tokenIssuer = getIssuer( token );

            if ( tokenIssuer == null )
                return false;

            return tokenIssuer.equals( issuer );
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * トークンに発行日時が設定されているかどうかを検証します。
     * 
     * @param token トークン
     * @return トークンに発行日時が設定されているかどうかを示すブール値
     */
    public static boolean verifyByIssueTime(String token)
    {
        try
        {
            Date issueTime = getIssueTime( token );

            if ( issueTime == null )
                return false;

            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * トークンに有効期限が設定されているかどうかを検証します。<br>
     * トークンが有効期限内かどうかは検証されません。<br>
     * 
     * @param token トークン
     * @return トークンに有効期限が設定されているかどうかを示すブール値
     */
    public static boolean verifyByExpirationTime(String token)
    {
        try
        {
            Date expirationTime = getExpirationTime( token );

            if ( expirationTime == null )
                return false;

            return true;
        }
        catch ( Exception e )
        {
            return false;
        }
    }

    /**
     * トークンの有効期限を判定します。<br>
     * トークンが正しいものかどうかは検証されません。<br>
     * 有効期限が設定されていない、もしくは取得に失敗した場合、{@link IllegalArgumentException}が発生します。<br>
     * 例外の発生を回避したい場合は、{@link #verifyByExpirationTime(String token)}を用いて事前にトークンの検証を行ってください。<br>
     * 
     * @param token トークン
     * @return トークンが有効期限内かどうかを示すブール値
     * @throws IllegalArgumentException トークンに有効期限が設定されていない、もしくは取得に失敗した場合
     */
    public static boolean isWithinExpirationTime(String token)
    {
        Date expirationTime = getExpirationTime( token );

        if ( expirationTime == null )
            throw new IllegalArgumentException( "tokenには有効期限が設定されていません。" );

        return (new Date()).before( expirationTime );
    }
}
