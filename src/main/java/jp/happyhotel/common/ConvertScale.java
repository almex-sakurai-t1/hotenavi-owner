/*
 * @(#)ConvertScale.java 1.00
 * 2010/05/10 Copyright (C) ALMEX Inc. 2009
 * �k�ڕϊ��N���X
 */

package jp.happyhotel.common;

import java.io.Serializable;

/**
 * �k�ڕϊ��N���X
 * 
 * @author S.Tashiro
 * @version 1.00 2010/05/10
 */
public class ConvertScale implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 9174000858624018451L;

    /**
     * �i�K����k�ڂ��擾����
     * 
     * @param scale �X�P�[���B�k��
     * @return ��������(5000�`3000000�܂ł̏k��)
     * @see "-4�F5000<br>
     *      -3�F5000<br>
     *      -2�F10000<br>
     *      -1�F25000<br>
     *      0�F70000<br>
     *      1�F250000<br>
     *      2�F500000<br>
     *      3�F1000000<br>
     *      4�F3000000"
     */
    public static int getScaleFromLevel(int scale)
    {
        int dispScale;

        switch( scale )
        {
            case -4:
                dispScale = 5000;
                break;
            case -3:
                dispScale = 5000;
                break;
            case -2:
                dispScale = 10000;
                break;
            case -1:
                dispScale = 25000;
                break;
            case 0:
                dispScale = 70000;
                break;
            case 1:
                dispScale = 250000;
                break;
            case 2:
                dispScale = 500000;
                break;
            case 3:
                dispScale = 1000000;
                break;
            case 4:
                dispScale = 3000000;
                break;
            default:
                dispScale = 70000;
        }
        return(dispScale);
    }

    /**
     * �k�ڂ��獡�̒i�K���擾����
     * 
     * @param scale �X�P�[���B�k��
     * @return ��������(5000�`3000000�܂ł̏k��)
     * @see "5000�F-3<br>
     *      10000�F-2<br>
     *      25000�F-1<br>
     *      70000�F&nbsp;0<br>
     *      250000�F&nbsp;1<br>
     *      500000�F&nbsp;2<br>
     *      1000000�F&nbsp;3<br>
     *      3000000�F&nbsp;4"
     */
    public static int getLevelFromScale(int scale)
    {
        int dispScale;

        switch( scale )
        {
            case 5000:
                dispScale = -3;
                break;
            case 10000:
                dispScale = -2;
                break;
            case 25000:
                dispScale = -1;
                break;
            case 70000:
                dispScale = 0;
                break;
            case 250000:
                dispScale = 1;
                break;
            case 500000:
                dispScale = 2;
                break;
            case 1000000:
                dispScale = 3;
                break;
            case 3000000:
                dispScale = 4;
                break;

            default:
                dispScale = 0;
        }
        return(dispScale);
    }
}
