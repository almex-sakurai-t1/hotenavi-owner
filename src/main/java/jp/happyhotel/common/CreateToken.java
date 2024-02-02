/*
 * @(#)CreateToken.java 1.00 2012/11/01 Copyright (C) ALMEX Inc. 2012 �g�[�N���쐬�N���X
 */

package jp.happyhotel.common;

import java.io.Serializable;

/**
 * <p>
 * �g�[�N���쐬�N���X
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

    // SUID��ϊ�����
    // �L�[
    public static byte[]      key              = "axpol ptmhyeeahl".getBytes();
    // �Í��x�N�^�[�iInitialization Vector�F�������x�N�g���j
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
     * �g�[�N���쐬����
     * 
     * @param token �g�[�N���쐬�Ώە�����
     * @param kind �敪�i1:�����R�[�X����A2:�v���~�A���R�[�X����A3:�����R�[�X�މ�A4:�v���~�A���R�[�X�މ�j
     * @return ��������(true:����,false:�ُ�)
     */
    public String create(String token, int kind)
    {

        // �Í���
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
                    // �ߋ��̓��t�ƂȂ邽�߂ӂ��킵���Ȃ��̂ŃG���[
                    break;
                }
                else if ( Integer.parseInt( strDate ) > Integer.parseInt( DateEdit.getDate( 2 ) ) )
                {
                    Logging.info( "date1 check error" );
                    // �������ƂȂ�ӂ��킵���Ȃ��̂ŃG���[
                    break;
                }
                if ( Integer.parseInt( strTime ) < 0 && Integer.parseInt( strTime ) >= 240000 )
                {
                    Logging.info( "time check error" );
                    // �����ɂӂ��킵���Ȃ��̂ŃG���[
                    break;
                }
                // �敪�������A�L���ȊO�̏ꍇ
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
