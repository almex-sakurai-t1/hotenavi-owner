/*
 * @(#)StringFormat.java  2.00 2004/03/18
 *
 * Copyright (C) ALMEX Inc. 2004
 *
 * ������ҏWBean
 */

package com.hotenavi2.common;

import java.io.*;
import java.lang.*;

/**
 * ������̕ҏW���s���N���X�B
 *
 * @author  S.Shiiya
 * @version 2.00 2004/03/18
 */
public class StringFormat implements Serializable
{

    /**
     * TcpClient�����������܂��B
     */
    public StringFormat()
    {
    }

    /**
     *  ���l�߃X�y�[�X���߃t�H�[�}�b�g
     *  �ҏW�㌅�������̌�����菬�����ꍇ�̓J�b�g����
     *
     *  @param strOrg �ҏW�O������
     *  @param nLength �ҏW�㕶����
     *  @return String �ҏW�㕶����
     */
    public String leftFitFormat(String strOrg, int nLength)
    {
        int       i;
        int       full = 0;
        int       strlen;
        String    buff;
        byte      bytebuff[];
        byte      cutbuff[];

        buff = new String(strOrg);
        cutbuff = new byte[nLength];

        try
        {
            bytebuff = strOrg.getBytes("Windows-31J");
            for( i = 0 ; i < nLength ; i++ )
            {
                if( i < bytebuff.length )
                {
                    cutbuff[i] = bytebuff[i];
                }
                else
                {
                    cutbuff[i] = ' ';
                }
            }

            return( new String(cutbuff,"Windows-31J") );
        }
        catch( Exception e )
        {
            return( "" );
        }
    }

    /**
     *  �E�l�߃X�y�[�X���߃t�H�[�}�b�g
     *  �ҏW�㌅�������̌�����菬�����ꍇ�̓J�b�g����
     *
     *  @param strOrg �ҏW�O������
     *  @param nLength �ҏW�㕶����
     *  @return String �ҏW�㕶����
     */
    public String rightFitFormat(String strOrg, int nLength)
    {
        int       i;
        int       full = 0;
        int       strlen;
        String    buff;
        String    space;

        buff = new String(strOrg);
        space = "";

        strlen = buff.length();

        for( i = 0 ; i < strlen ; i ++ )
        {
            // �S�p�����`�F�b�N
            if( ( buff.charAt(i) > 0x0019 && buff.charAt(i) < 0x0080 ) ||
                ( buff.charAt(i) > 0xFF61 && buff.charAt(i) < 0xFF9F ) )
            {
            }
            else
            {
                full++;
            }
        }

        if( (nLength - strlen) >= 0 )
        {
            for( i = 0 ; i < nLength - strlen - full ; i++ )
            {
                space = space + " ";
            }

            buff = space + buff;
        }
        else
        {
            buff = buff.substring(0, nLength);
        }

        return( buff );
    }

    /**
     *  �L��������擾�i�S�p�X�y�[�X�J�b�g�j
     *  �ҏW�㌅�������̌�����菬�����ꍇ�̓J�b�g����
     *
     *  @param strOrg �ҏW�O������
     *  @return String �ҏW�㕶����
     */
     public String cutFullSpace(String strOrg) throws IOException
     {
         int       i;
         int       point;
         String    data;
         String    buff;

         point = -1;

         // �S�p�ʒu���`�F�b�N����
         for( i = 0 ; i < strOrg.length() ; i += 2 )
         {
             data = new String(strOrg.substring(i, i+2).getBytes("8859_1"), "Windows-31J");
             if( data.compareTo("�@") == 0 )
             {
                 if( point == -1 )
                 {
                     point = i;
                 }
             }
             else
             {
                 point = -1;
             }
         }

         if( point >= 0 )
         {
             buff = strOrg.substring(0, point);
         }
         else
         {
             buff = strOrg;
         }

         return( buff );
     }

    /**
    *  �����񐔒l�E�L���`�F�b�N����
    *
    *  @param orgText �`�F�b�N������
    *  @return int 0:�Ȃ�,1:���l�E�L������
    */
    public static int checkNumSymbolString(String orgText)
    {
        int       i;
        int       ret;
        int       full = 0;
        int       strlen;
        byte      bytebuff[];
        char      onechar = 0x00;
        String    buff;
        String    space;

        if( orgText == null )
        {
            return( 1 );
        }

        try
        {
            buff = new String(orgText.getBytes("Windows-31J"), "8859_1");
            ret  = 0;

            strlen = buff.length();

            for( i = 0 ; i < strlen ; i ++ )
            {
                if( full == 0 )
                {
                    // ���l�E�L���`�F�b�N
                    if( ( buff.charAt(i) >= 0x00 && buff.charAt(i) <= 0x40 ) || 
                        ( buff.charAt(i) >= 0x5b && buff.charAt(i) <= 0x60 ) ||
                        ( buff.charAt(i) >= 0x7b && buff.charAt(i) <= 0x7f ) ||
                        ( buff.charAt(i) >= 0xa1 && buff.charAt(i) <= 0xa5 ) )
                    {
                        ret = 1;
                        break;
                    }

                    // 2�o�C�g�����`�F�b�N
                    if( ( buff.charAt(i) >= 0x81 && buff.charAt(i) <= 0x9F ) ||
                        ( buff.charAt(i) >= 0xe0 && buff.charAt(i) <= 0xFC ) )

                    {
                        onechar = buff.charAt(i);
                        full++;
                    }
                }
                else
                {
                    // 1�o�C�g�ڂ̃R�[�h�ŏ����𕪂���
                    switch( onechar )
                    {
                      case 0x81:
                        if( buff.charAt(i) >= 0x40 && buff.charAt(i) <= 0x51 )
                        {
                            ret = 1;
                        }
                        if( buff.charAt(i) >= 0x5C && buff.charAt(i) <= 0xFC )
                        {
                            ret = 1;
                        }
                        break;

                      case 0x82:
                        if( buff.charAt(i) >= 0x4F && buff.charAt(i) <= 0x58 )
                        {
                            ret = 1;
                        } 
                        break;

                      case 0x83:
                        if( buff.charAt(i) >= 0x9F && buff.charAt(i) <= 0xFC )
                        {
                            ret = 1;
                        } 
                        break;

                      case 0x84:
                      case 0x85:
                      case 0x86:
                      case 0x87:
                        if( buff.charAt(i) >= 0x40 && buff.charAt(i) <= 0xFC )
                        {
                            ret = 1;
                        } 
                        break;

                      case 0x88:
                        if( buff.charAt(i) >= 0x40 && buff.charAt(i) <= 0x9E )
                        {
                            ret = 1;
                        } 
                        break;

                      case 0xF0:
                        if( buff.charAt(i) >= 0x40 )
                        {
                            ret = 1;
                        } 
                        break;
                    }

                    full = 0;

                    if( ret == 1 )
                    {
                        break;
                    }
                }
            }
        }
        catch( Exception e )
        {
            System.out.println(e.toString());
            ret = 1;
        }

        return( ret );
    }

    public String changeUnicode(String moto_data)
    {
        int     i;
        StringBuffer sb = new StringBuffer();

        for( i = 0 ; i < moto_data.length() ; i++ )
        {
            char c = moto_data.charAt(i);
            switch( c )
            {
              case '\u301C':
                sb.append("\uFF5E");
                break;

              case '\u2016':
                sb.append("\u2225");
                break;

              case '\u2212':
                sb.append("\uFF0D");
                break;

              case '\u00A2':
                sb.append("\uFFE0");
                break;

              case '\u00A3':
                sb.append("\uFFE1");
                break;

              case '\u00AC':
                sb.append("\uFFE2");
                break;

              default:
                sb.append(c);
                break;
            }
        }

        return( sb.toString() );
}

}
