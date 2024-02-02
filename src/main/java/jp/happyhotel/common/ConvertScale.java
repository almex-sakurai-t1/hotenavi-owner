/*
 * @(#)ConvertScale.java 1.00
 * 2010/05/10 Copyright (C) ALMEX Inc. 2009
 * 縮尺変換クラス
 */

package jp.happyhotel.common;

import java.io.Serializable;

/**
 * 縮尺変換クラス
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
     * 段階から縮尺を取得する
     * 
     * @param scale スケール。縮尺
     * @return 処理結果(5000～3000000までの縮尺)
     * @see "-4：5000<br>
     *      -3：5000<br>
     *      -2：10000<br>
     *      -1：25000<br>
     *      0：70000<br>
     *      1：250000<br>
     *      2：500000<br>
     *      3：1000000<br>
     *      4：3000000"
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
     * 縮尺から今の段階を取得する
     * 
     * @param scale スケール。縮尺
     * @return 処理結果(5000～3000000までの縮尺)
     * @see "5000：-3<br>
     *      10000：-2<br>
     *      25000：-1<br>
     *      70000：&nbsp;0<br>
     *      250000：&nbsp;1<br>
     *      500000：&nbsp;2<br>
     *      1000000：&nbsp;3<br>
     *      3000000：&nbsp;4"
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
