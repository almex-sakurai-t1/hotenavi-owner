/*
 * @(#)CreateToken.java 1.00 2012/11/01 Copyright (C) ALMEX Inc. 2012 トークン作成クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;

/**
 * <p>
 * トークン作成クラス
 * </p>
 * 
 * @author S.Tashiro
 * @version 1.00 2012/11/01
 */
public class CreateToken implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -4856108172406548350L;

    // SUIDを変換する
    // キー
    public static byte[]      key              = "axpol ptmhyeeahl".getBytes();
    // 暗号ベクター（Initialization Vector：初期化ベクトル）
    public static byte[]      ivBytes          = "s h t t i s n h ".getBytes();

    private int               date             = 0;
    private int               time             = 0;
    private int               kind             = 0;
    private String            suid             = "";

    public int getDate()
    {
        return(this.date);
    }

    public int getTime()
    {
        return(this.time);
    }

    public int getKind()
    {
        return(this.kind);
    }

    public String getSuid()
    {
        return(this.suid);
    }

    /**
     * トークン作成処理
     * 
     * @param token トークン作成対象文字列
     * @param kind 区分（1:無料コース入会、2:プレミアムコース入会、3:無料コース退会、4:プレミアムコース退会）
     * @return 処理結果(true:正常,false:異常)
     */
    public String create(String token, int kind)
    {

        // 暗号化
        String encode;

        String date = DateEdit.getDate( 2 );
        String time = DateEdit.getTime( 1 );
        time = String.format( "%1$06d", Integer.parseInt( time ) );
        encode = date + " " + time + " " + kind + "" + token;

        encode = EncodeData.encodeString( key, ivBytes, encode );
        return(encode);
    }

    public boolean decodeCheck(String token)
    {
        boolean ret;
        String decode;
        String strDate;
        String strTime;
        String strKind;
        String strSuid;

        ret = false;

        try
        {

            decode = DecodeData.decodeString( key, ivBytes, token );

            Logging.info( decode );

            strDate = decode.substring( 0, 8 );
            strTime = decode.substring( 9, 15 );
            strKind = decode.substring( 16, 17 );
            strSuid = decode.substring( 17 );

            while( true )
            {
                if ( Integer.parseInt( strDate ) <= 20121101 )
                {
                    Logging.info( "date check error" );
                    // 過去の日付となるためふさわしくないのでエラー
                    break;
                }
                else if ( Integer.parseInt( strDate ) > Integer.parseInt( DateEdit.getDate( 2 ) ) )
                {
                    Logging.info( "date1 check error" );
                    // 未来日となりふさわしくないのでエラー
                    break;
                }
                if ( Integer.parseInt( strTime ) < 0 && Integer.parseInt( strTime ) >= 240000 )
                {
                    Logging.info( "time check error" );
                    // 時刻にふさわしくないのでエラー
                    break;
                }
                // 区分が無料、有料以外の場合
                if ( Integer.parseInt( strKind ) < 1 && Integer.parseInt( strKind ) > 4 )
                {
                    Logging.info( "kind check error" );
                    break;
                }
                if ( strSuid.equals( "" ) != false )
                {
                    Logging.info( "strSuid check error" );
                    break;
                }
                ret = true;
                break;
            }
            if ( ret != false )
            {
                this.date = Integer.parseInt( strDate );
                this.time = Integer.parseInt( strTime );
                this.kind = Integer.parseInt( strKind );
                this.suid = strSuid;
                Logging.info( "CreateToken_suid:" + this.suid );
            }
        }
        catch ( Exception e )
        {
            Logging.error( "[CreateToken] Exception:" + e.toString() );
        }
        return ret;
    }
}
